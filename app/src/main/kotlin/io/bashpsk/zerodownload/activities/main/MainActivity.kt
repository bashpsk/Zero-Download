package io.bashpsk.zerodownload.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.RetainedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import dagger.hilt.android.AndroidEntryPoint
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore
import io.bashpsk.emptylibs.datastoreui.extension.getPreference
import io.bashpsk.zerodownload.core.datastore.datastore.datastore
import io.bashpsk.zerodownload.core.datastore.settings.PreferenceData
import io.bashpsk.zerodownload.core.model.settings.AppTheme
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.naviagtion.MainNavigation
import io.bashpsk.zerodownload.ui.theme.ZeroDownloadTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        installSplashScreen().setKeepOnScreenCondition {

            mainViewModel.isKeepSplashScreen.value
        }

        setContent {

            val navBackStack = rememberNavBackStack(NavScreen.Unknown)

            val getAppTheme by datastore.getPreference(
                key = PreferenceData.ApplicationTheme.key,
                initial = PreferenceData.ApplicationTheme.initial
            ).collectAsStateWithLifecycle(initialValue = PreferenceData.ApplicationTheme.initial)

            val getDynamicColor by datastore.getPreference(
                key = PreferenceData.DynamicColorTheme.key,
                initial = PreferenceData.DynamicColorTheme.initial
            ).collectAsStateWithLifecycle(initialValue = PreferenceData.DynamicColorTheme.initial)

            val getAppLanguage by datastore.getPreference(
                key = PreferenceData.ApplicationLanguage.key,
                initial = PreferenceData.ApplicationLanguage.initial
            ).collectAsStateWithLifecycle(initialValue = PreferenceData.ApplicationLanguage.initial)

            val navScreenChannel by mainViewModel.navScreenChannel.collectAsStateWithLifecycle(
                initialValue = null
            )

            RetainedEffect(intent, navBackStack) {

                if (navBackStack.contains(element = NavScreen.Unknown)) mainViewModel.onUIEvent(
                    uiEvent = MainUIEvent.SetNavChannel(
                        uri = intent.data.toString(),
                        type = intent.type ?: ""
                    )
                )

                onRetire { }
            }

            RetainedEffect(navScreenChannel) {

                navScreenChannel?.let { screen ->

                    navBackStack.add(element = screen)
                    navBackStack.remove(element = NavScreen.Unknown)
                }

                onRetire { }
            }

            CompositionLocalProvider(LocalDatastore provides datastore) {

                ZeroDownloadTheme(
                    isDarkTheme = AppTheme.getTheme(theme = getAppTheme),
                    isDynamicColor = getDynamicColor
                ) {

                    MainNavigation(navBackStack = navBackStack)
                }
            }
        }
    }
}