package io.bashpsk.zerodownload.feature.settings.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.core.ui.animation.itemBounceAnimation
import io.bashpsk.zerodownload.core.ui.topbar.GenericArrowTopBar
import io.bashpsk.zerodownload.core.ui.window.SettingsViewLayoutSize
import io.bashpsk.zerodownload.feature.settings.R
import io.bashpsk.zerodownload.feature.settings.ui.ApplicationLanguageSetting
import io.bashpsk.zerodownload.feature.settings.ui.ApplicationThemeSetting
import io.bashpsk.zerodownload.feature.settings.ui.DynamicColorThemeSetting

@Composable
fun GeneralSettingScreen(
    onNavigateBack: () -> Unit
) {

    val settingsLazyListState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {

            GenericArrowTopBar(
                title = stringResource(R.string.general_settings_screen),
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

                ApplicationThemeSetting(
                    modifier = Modifier.itemBounceAnimation()
                )
            }

            item {

                when {

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> DynamicColorThemeSetting(
                        modifier = Modifier.itemBounceAnimation()
                    )
                }
            }

            item {

                ApplicationLanguageSetting(
                    modifier = Modifier.itemBounceAnimation()
                )
            }
        }
    }
}