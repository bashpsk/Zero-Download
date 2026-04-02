package io.bashpsk.zerodownload.presentation.appscreen.setting

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
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.domain.resources.ConstantWindow
import io.bashpsk.zerodownload.domain.topbar.TopAppBarType
import io.bashpsk.zerodownload.presentation.topbar.DownloadsSettingTopBar

@OptIn(ExperimentalMaterial3Api::class)
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

            DownloadsSettingTopBar(
                scrollBehavior = scrollBehavior,
                topAppBarType = TopAppBarType.Arrow,
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
            columns = GridCells.Adaptive(minSize = ConstantWindow.SETTING_VIEW),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {

            item {

            }
        }
    }
}