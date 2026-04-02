package io.bashpsk.zerodownload.presentation.appscreen.settings

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.domain.navigation.AppSettingCategory
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.zerodownload.domain.resources.ConstantWindow
import io.bashpsk.zerodownload.domain.topbar.TopAppBarType
import io.bashpsk.zerodownload.presentation.settings.SettingCategoryView
import io.bashpsk.zerodownload.presentation.topbar.SettingsTopBar
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun AppSettingsScreen(
    crossinline onNavigateScreen: (navScreen: NavScreen) -> Unit,
    noinline onNavigateBack: () -> Unit
) {

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val settingCategoryList = remember { AppSettingCategory.entries.toImmutableList() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            SettingsTopBar(
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
            columns = GridCells.Adaptive(minSize = ConstantWindow.SETTINGS_CATEGORY),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {

            items(
                items = settingCategoryList,
                key = { settingCategory -> settingCategory.name }
            ) { settingCategory ->

                SettingCategoryView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    settingCategory = settingCategory,
                    onOpenSettings = onNavigateScreen
                )
            }
        }
    }
}