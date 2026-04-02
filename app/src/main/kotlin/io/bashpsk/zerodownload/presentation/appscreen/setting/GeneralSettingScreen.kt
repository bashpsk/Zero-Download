package io.bashpsk.zerodownload.presentation.appscreen.setting

import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import io.bashpsk.zerodownload.presentation.appui.settings.ApplicationLanguageSetting
import io.bashpsk.zerodownload.presentation.appui.settings.ApplicationThemeSetting
import io.bashpsk.zerodownload.presentation.appui.settings.DynamicColorThemeSetting
import io.bashpsk.zerodownload.presentation.topbar.GeneralSettingTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun GeneralSettingScreen(
    noinline onNavigateBack: () -> Unit
) {

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            GeneralSettingTopBar(
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

                ApplicationThemeSetting(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )
                )
            }

            item {

                when {

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> DynamicColorThemeSetting(
                        modifier = Modifier.animateItem(
                            fadeInSpec = tween(durationMillis = 250),
                            fadeOutSpec = tween(durationMillis = 100),
                            placementSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            )
                        )
                    )
                }
            }

            item {

                ApplicationLanguageSetting(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )
                )
            }
        }
    }
}