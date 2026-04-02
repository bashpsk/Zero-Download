package io.bashpsk.zerodownload.presentation.navigation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.zerodownload.presentation.appscreen.home.HomeScreen
import io.bashpsk.zerodownload.presentation.appscreen.setting.DownloaderSettingScreen
import io.bashpsk.zerodownload.presentation.appscreen.setting.DownloadsSettingScreen
import io.bashpsk.zerodownload.presentation.appscreen.setting.GeneralSettingScreen
import io.bashpsk.zerodownload.presentation.appscreen.settings.AppSettingsScreen
import io.bashpsk.zerodownload.presentation.appscreen.unknown.UnknownScreen

@Composable
fun MainNavHost(navBackStack: NavBackStack<NavKey>) {

    val enterTransition = fadeIn() + slideInVertically(
        initialOffsetY = { offset -> -offset },
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioHighBouncy
        )
    )

    val exitTransition = fadeOut() + slideOutVertically(
        targetOffsetY = { offset -> offset },
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioHighBouncy
        )
    )

    val popEnterTransition = fadeIn() + slideInHorizontally(
        initialOffsetX = { offset -> -offset },
        animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing)
    )

    val popExitTransition = fadeOut() + slideOutHorizontally(
        targetOffsetX = { offset -> offset },
        animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing)
    )

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = navBackStack,
        onBack = navBackStack::removeLastSafe,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { enterTransition togetherWith exitTransition },
        popTransitionSpec = { popEnterTransition togetherWith popExitTransition },
        predictivePopTransitionSpec = defaultPredictivePopTransitionSpec(),
        entryProvider = entryProvider {

            entry<NavScreen.Unknown> { backStackEntry ->

                UnknownScreen()
            }

            entry<NavScreen.Home> { backStackEntry ->

                HomeScreen(
                    onNavigateScreen = navBackStack::addSafe
                )
            }

            entry<NavScreen.AppSettings> { backStackEntry ->

                AppSettingsScreen(
                    onNavigateScreen = navBackStack::addSafe,
                    onNavigateBack = navBackStack::removeLastSafe
                )
            }

            entry<NavScreen.GeneralSetting> { backStackEntry ->

                GeneralSettingScreen(onNavigateBack = navBackStack::removeLastSafe)
            }

            entry<NavScreen.DownloaderSetting> { backStackEntry ->

                DownloaderSettingScreen(onNavigateBack = navBackStack::removeLastSafe)
            }

            entry<NavScreen.DownloadsSetting> { backStackEntry ->

                DownloadsSettingScreen(onNavigateBack = navBackStack::removeLastSafe)
            }
        }
    )
}