package io.bashpsk.zerodownload.feature.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.core.navigation.model.AppSettingCategory
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.core.ui.animation.itemBounceAnimation
import io.bashpsk.zerodownload.core.ui.settings.SettingCategoryView
import io.bashpsk.zerodownload.core.ui.topbar.GenericArrowTopBar
import io.bashpsk.zerodownload.core.ui.window.SettingsCategoryLayoutSize
import io.bashpsk.zerodownload.feature.settings.R
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AppSettingsScreen(
    onNavigateScreen: (navScreen: NavScreen) -> Unit,
    onNavigateBack: () -> Unit
) {

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val settingCategoryList = remember { AppSettingCategory.entries.toImmutableList() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            GenericArrowTopBar(
                title = stringResource(R.string.settings_screen),
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
            columns = GridCells.Adaptive(minSize = SettingsCategoryLayoutSize),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {

            items(
                items = settingCategoryList,
                key = { settingCategory -> settingCategory.name }
            ) { settingCategory ->

                SettingCategoryView(
                    modifier = Modifier.itemBounceAnimation(),
                    settingCategory = settingCategory,
                    onOpenSettings = onNavigateScreen
                )
            }
        }
    }
}