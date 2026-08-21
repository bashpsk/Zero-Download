package io.bashpsk.zerodownload.core.navigation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.common.R
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen

enum class ScreenCategory(
    val id: Int,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: NavScreen
) {

    Home(
        id = 0,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = NavScreen.Home
    ),
    Downloads(
        id = 1,
        label = "Downloads",
        selectedIcon = Icons.AutoMirrored.Filled.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks,
        route = NavScreen.Downloads
    ),
    Settings(
        id = 2,
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = NavScreen.Settings
    );

    companion object {

        fun find(id: Int): ScreenCategory {

            return when (id) {

                0 -> Home
                1 -> Downloads
                2 -> Settings
                else -> Home
            }
        }

        fun findFromRoute(route: NavScreen): ScreenCategory {

            return when (route) {

                is NavScreen.Home -> Home
                is NavScreen.Downloads -> Downloads
                is NavScreen.Settings -> Settings
                else -> Home
            }
        }
    }
}

enum class AppSettingCategory(
    @param:StringRes
    val title: Int = R.string.none,
    @param:StringRes
    val description: Int = R.string.none,
    val icon: ImageVector = Icons.Filled.QuestionMark,
    val screen: NavScreen = NavScreen.Unknown
) {

    General(
        title = R.string.general_settings,
        description = R.string.general_settings_desc,
        icon = Icons.Filled.SettingsApplications,
        screen = NavScreen.GeneralSetting
    ),
    Downloader(
        title = R.string.downloader_settings,
        description = R.string.downloader_settings_desc,
        icon = Icons.Filled.CloudDownload,
        screen = NavScreen.DownloaderSetting
    ),
    Downloads(
        title = R.string.downloads_settings,
        description = R.string.downloads_settings_desc,
        icon = Icons.Filled.FileDownload,
        screen = NavScreen.DownloadsSetting
    );

    companion object {

        @Composable
        @ReadOnlyComposable
        fun AppSettingCategory.toTitle(): String {

            return stringResource(title)
        }

        @Composable
        @ReadOnlyComposable
        fun AppSettingCategory.toDescription(): String {

            return stringResource(description)
        }
    }
}