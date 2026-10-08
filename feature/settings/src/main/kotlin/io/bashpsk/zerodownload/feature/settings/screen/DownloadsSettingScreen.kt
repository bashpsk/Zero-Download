package io.bashpsk.zerodownload.feature.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.core.ui.topbar.GenericArrowTopBar
import io.bashpsk.zerodownload.core.ui.window.SettingsViewLayoutSize
import io.bashpsk.zerodownload.feature.settings.R

@Composable
inline fun DownloadsSettingScreen(
    noinline onNavigateBack: () -> Unit
) {

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            GenericArrowTopBar(
                title = stringResource(R.string.downloads_settings_screen),
                scrollBehavior = scrollBehavior,
                onNavigationClick = onNavigateBack
            )
        }
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

            }
        }
    }
}