package io.bashpsk.zerodownload.feature.home.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.core.ui.dialogs.MediaConfirmDialog
import io.bashpsk.zerodownload.core.ui.dialogs.MediaDetailDialog
import io.bashpsk.zerodownload.core.ui.fab.DownloadSmallFab
import io.bashpsk.zerodownload.core.ui.permissions.FileWritePermission
import io.bashpsk.zerodownload.core.ui.permissions.rememberManageStoragePermission
import io.bashpsk.zerodownload.core.ui.window.MediaViewLayoutSize
import io.bashpsk.zerodownload.feature.home.bottombar.HomeBottomBar
import io.bashpsk.zerodownload.feature.home.event.HomeUIEvent
import io.bashpsk.zerodownload.feature.home.topbar.HomeTopBar
import io.bashpsk.zerodownload.feature.home.ui.MediaSearchStateView

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
inline fun HomeScreen(crossinline onNavigateScreen: (navScreen: NavScreen) -> Unit) {

    val viewModel = hiltViewModel<HomeViewModel>()

    val context = LocalContext.current
    val activity = LocalActivity.current
    val datastore = LocalDatastore.current
    val homePullToRefreshState = rememberPullToRefreshState()
    val homeLazyGridState = rememberLazyGridState()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val snakeBarCoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mediaDetailsDialogVisibleState = remember { MutableTransitionState(false) }
    val mediaConfirmDialogVisibleState = remember { MutableTransitionState(false) }

    val isOptionMenu by viewModel.isOptionMenu.collectAsStateWithLifecycle()
    val searchMediaState by viewModel.searchMediaState.collectAsStateWithLifecycle()
    val selectedPlaylistMedias by viewModel.selectedPlaylistMedias.collectAsStateWithLifecycle()
    val isScreenRefreshing by viewModel.isScreenRefreshing.collectAsStateWithLifecycle()
    val isMediaSelect by viewModel.isMediaSelect.collectAsStateWithLifecycle()
    val selectedAudioFormat by viewModel.selectedAudioFormat.collectAsStateWithLifecycle()
    val selectedVideoFormat by viewModel.selectedVideoFormat.collectAsStateWithLifecycle()

    val filePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val fileManagePermissionState = rememberManageStoragePermission()

    val filePermissionVisibleState by remember(fileManagePermissionState, filePermissionState) {
        derivedStateOf {
            when {

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> !fileManagePermissionState
                else -> !filePermissionState.status.isGranted
            }
        }
    }

    val isFabExpanded by remember {
        derivedStateOf { homeLazyGridState.firstVisibleItemIndex == 0 }
    }

    BackHandler(enabled = isMediaSelect) {

        when {

            isMediaSelect -> {

                viewModel.onUIEvent(uiEvent = HomeUIEvent.MediaSelect(isVisible = false))
                viewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            }

            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            HomeTopBar(
                isOptionMenu = isOptionMenu,
                onNavigateScreen = onNavigateScreen,
                onUIEvent = viewModel::onUIEvent,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {

            if (isMediaSelect) DownloadSmallFab(
                expanded = isFabExpanded,
                onClick = { mediaConfirmDialogVisibleState.targetState = true }
            )
        },
        bottomBar = {

            HomeBottomBar(
                isMediaSelect = isMediaSelect,
                onNavigateScreen = onNavigateScreen,
                onUIEvent = viewModel::onUIEvent,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        MediaDetailDialog(
            dialogVisibleState = mediaDetailsDialogVisibleState,
            searchState = searchMediaState
        )

        MediaConfirmDialog(
            dialogVisibleState = mediaConfirmDialogVisibleState,
            searchState = searchMediaState,
            audioFormat = selectedAudioFormat,
            videoFormat = selectedVideoFormat,
            selectedPlaylistMedias = selectedPlaylistMedias,
            onStartPlayMedia = { media, audio, video ->

                viewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            },
            onDownloadMedia = { media, audio, video,videoExt,audioExt ->

                viewModel.onUIEvent(
                    uiEvent = HomeUIEvent.MediaDownloadCombined(
                        media = media,
                        audio = audio,
                        video = video,
                        videoExt = videoExt,
                        audioExt = audioExt
                    )
                )

                viewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            },
            onDownloadPlaylist = { playlist, format, videoQuality, audioQuality,videoExt,audioExt ->

                viewModel.onUIEvent(
                    uiEvent = HomeUIEvent.MediaDownloadPlaylist(
                        playlist = playlist,
                        format = format,
                        videoQuality = videoQuality,
                        audioQuality = audioQuality,
                        videoExt = videoExt,
                        audioExt = audioExt
                    )
                )
            }
        )

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues = paddingValues),
            state = homePullToRefreshState,
            isRefreshing = isScreenRefreshing,
            indicator = {

                PullToRefreshDefaults.LoadingIndicator(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(paddingValues = paddingValues),
                    state = homePullToRefreshState,
                    isRefreshing = isScreenRefreshing
                )
            },
            onRefresh = {}
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                state = homeLazyGridState,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                columns = GridCells.Adaptive(minSize = MediaViewLayoutSize),
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {

                item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                    FileWritePermission(
                        visibleState = filePermissionVisibleState,
                        onPermissionResult = {}
                    )
                }

                MediaSearchStateView(
                    searchState = searchMediaState,
                    mediaDetailsDialogVisibleState = mediaDetailsDialogVisibleState,
                    selectedAudioFormat = selectedAudioFormat,
                    selectedVideoFormat = selectedVideoFormat,
                    isMediaSelect = isMediaSelect,
                    selectedPlaylistMedias = selectedPlaylistMedias,
                    onVideoClick = { format ->

                        viewModel.onUIEvent(HomeUIEvent.SetSelectVideoFormat(media = format))
                        mediaConfirmDialogVisibleState.targetState = true
                    },
                    onVideoLongClick = { format ->

                        viewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        viewModel.onUIEvent(HomeUIEvent.SetSelectVideoFormat(media = format))
                    },
                    onAudioClick = { format ->

                        viewModel.onUIEvent(HomeUIEvent.SetSelectAudioFormat(media = format))
                        mediaConfirmDialogVisibleState.targetState = true
                    },
                    onAudioLongClick = { format ->

                        viewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        viewModel.onUIEvent(HomeUIEvent.SetSelectAudioFormat(media = format))
                    },
                    onSelectPlaylistMedia = { media ->

                        viewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        viewModel.onUIEvent(HomeUIEvent.SetSelectPlaylistMedia(media = media))
                    }
                )
            }
        }
    }
}