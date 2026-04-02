package io.bashpsk.zerodownload.presentation.appscreen.home

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.events.HomeUIEvent
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.zerodownload.domain.resources.ConstantWindow
import io.bashpsk.zerodownload.presentation.appui.home.MediaSearchStateView
import io.bashpsk.zerodownload.presentation.appui.permissions.FileWritePermission
import io.bashpsk.zerodownload.presentation.dialogs.MediaConfirmDialog
import io.bashpsk.zerodownload.presentation.dialogs.MediaDetailDialog
import io.bashpsk.zerodownload.presentation.dialogs.SearchLinkInputDialog
import io.bashpsk.zerodownload.presentation.permission.rememberManageStoragePermission
import io.bashpsk.zerodownload.presentation.topbar.HomeTopBar
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
inline fun HomeScreen(crossinline onNavigateScreen: (navScreen: NavScreen) -> Unit) {

    val mainViewModel = hiltViewModel<HomeViewModel>()

    val context = LocalContext.current
    val activity = LocalActivity.current
    val datastore = LocalDatastore.current
    val homePullToRefreshState = rememberPullToRefreshState()
    val homeLazyGridState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snakeBarCoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val mediaDetailsDialogVisibleState = remember { MutableTransitionState(false) }
    val mediaConfirmDialogVisibleState = remember { MutableTransitionState(false) }
    val linkInputDialogVisibleState = remember { MutableTransitionState(false) }

    val isOptionMenu by mainViewModel.isOptionMenu.collectAsStateWithLifecycle()
    val searchMediaState by mainViewModel.searchMediaState.collectAsStateWithLifecycle()
    val selectedPlaylistMedias by mainViewModel.selectedPlaylistMedias.collectAsStateWithLifecycle()
    val isScreenRefreshing by mainViewModel.isScreenRefreshing.collectAsStateWithLifecycle()
    val isMediaSelect by mainViewModel.isMediaSelect.collectAsStateWithLifecycle()
    val selectedAudioFormat by mainViewModel.selectedAudioFormat.collectAsStateWithLifecycle()
    val selectedVideoFormat by mainViewModel.selectedVideoFormat.collectAsStateWithLifecycle()

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

    BackHandler(enabled = true) {

        when {

            isMediaSelect -> {

                mainViewModel.onUIEvent(uiEvent = HomeUIEvent.MediaSelect(isVisible = false))
                mainViewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            }

            else -> activity?.finishAfterTransition()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            HomeTopBar(
                scrollBehavior = scrollBehavior,
                isOptionMenu = isOptionMenu,
                isMediaSelect = isMediaSelect,
                onOptionMenu = { isVisible ->

                    mainViewModel.onUIEvent(uiEvent = HomeUIEvent.OptionMenu(isVisible))
                },
                onMediaSelect = { isVisible ->

                    mainViewModel.onUIEvent(uiEvent = HomeUIEvent.MediaSelect(isVisible))
                    mainViewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
                },
                onDownloadClick = {

                    mediaConfirmDialogVisibleState.targetState = true
                },
                onNavigateScreen = onNavigateScreen
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                expanded = isFabExpanded,
                icon = {

                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.link_input_label)
                    )
                },
                text = {

                    Text(
                        text = stringResource(R.string.link_input_label),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                onClick = {

                    linkInputDialogVisibleState.targetState = true
                }
            )
        }
    ) { paddingValues ->

        SearchLinkInputDialog(
            dialogVisibleState = linkInputDialogVisibleState,
            onSearchClick = { link ->

                mainViewModel.onUIEvent(uiEvent = HomeUIEvent.MediaSearch(link = link))
            }
        )

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

                mainViewModel.onUIEvent(
                    uiEvent = HomeUIEvent.StartMediaPlayer(
                        media = media,
                        audio = audio,
                        video = video
                    )
                )

                mainViewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            },
            onDownloadMedia = { media, audio, video,videoExt,audioExt ->

                mainViewModel.onUIEvent(
                    uiEvent = HomeUIEvent.MediaDownloadCombined(
                        media = media,
                        audio = audio,
                        video = video,
                        videoExt = videoExt,
                        audioExt = audioExt
                    )
                )

                mainViewModel.onUIEvent(uiEvent = HomeUIEvent.ResetSelectedFormat)
            },
            onDownloadPlaylist = { playlist, format, videoQuality, audioQuality,videoExt,audioExt ->

                mainViewModel.onUIEvent(
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

                PullToRefreshDefaults.Indicator(
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
                columns = GridCells.Adaptive(minSize = ConstantWindow.MEDIA_VIEW),
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

                        mainViewModel.onUIEvent(HomeUIEvent.SetSelectVideoFormat(media = format))
                        mediaConfirmDialogVisibleState.targetState = true
                    },
                    onVideoLongClick = { format ->

                        mainViewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        mainViewModel.onUIEvent(HomeUIEvent.SetSelectVideoFormat(media = format))
                    },
                    onAudioClick = { format ->

                        mainViewModel.onUIEvent(HomeUIEvent.SetSelectAudioFormat(media = format))
                        mediaConfirmDialogVisibleState.targetState = true
                    },
                    onAudioLongClick = { format ->

                        mainViewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        mainViewModel.onUIEvent(HomeUIEvent.SetSelectAudioFormat(media = format))
                    },
                    onSelectPlaylistMedia = { media ->

                        mainViewModel.onUIEvent(HomeUIEvent.MediaSelect(isVisible = true))
                        mainViewModel.onUIEvent(HomeUIEvent.SetSelectPlaylistMedia(media = media))
                    }
                )
            }
        }
    }
}