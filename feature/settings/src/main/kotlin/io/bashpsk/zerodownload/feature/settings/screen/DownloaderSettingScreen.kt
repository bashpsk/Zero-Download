package io.bashpsk.zerodownload.feature.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.zerodownload.core.ui.animation.itemBounceAnimation
import io.bashpsk.zerodownload.core.ui.topbar.GenericArrowTopBar
import io.bashpsk.zerodownload.core.ui.window.SettingsViewLayoutSize
import io.bashpsk.zerodownload.feature.settings.R
import io.bashpsk.zerodownload.feature.settings.event.DownloaderSettingUIEvent
import io.bashpsk.zerodownload.feature.settings.ui.YtDlUpdateSetting
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Composable
inline fun DownloaderSettingScreen(
    noinline onNavigateBack: () -> Unit
) {

    val mainViewModel = hiltViewModel<DownloaderSettingViewModel>()

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarCoroutineScope = rememberCoroutineScope()

    val isYtDlUpdating by mainViewModel.isYtDlUpdating.collectAsStateWithLifecycle()

    val libraryRunningMessage = stringResource(R.string.library_update_already_running_message)
    val libraryStartingMessage = stringResource(R.string.library_update_starting_message)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            GenericArrowTopBar(
                title = stringResource(R.string.downloader_settings_screen),
                scrollBehavior = scrollBehavior,
                onNavigationClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues = paddingValues),
            state = settingsLazyListState,
            contentPadding = paddingValues,
            columns = GridCells.Adaptive(minSize = SettingsViewLayoutSize),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {

            item {

                YtDlUpdateSetting(
                    modifier = Modifier.itemBounceAnimation(),
                    onUpdateYtDl = {

                        when (isYtDlUpdating) {

                            true -> {

                                snackbarCoroutineScope.coroutineContext.cancelChildren()

                                snackbarCoroutineScope.launch {

                                    snackbarHostState.showSnackbar(message = libraryRunningMessage)
                                }
                            }

                            false -> {

                                mainViewModel.onUIEvent(DownloaderSettingUIEvent.UpdateYtDl)
                                snackbarCoroutineScope.coroutineContext.cancelChildren()

                                snackbarCoroutineScope.launch {

                                    snackbarHostState.showSnackbar(message = libraryStartingMessage)
                                }
                            }

                            else -> {}
                        }
                    }
                )
            }
        }
    }
}