package io.bashpsk.zerodownload.domain.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.R

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