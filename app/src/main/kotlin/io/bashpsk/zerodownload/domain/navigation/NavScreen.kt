package io.bashpsk.zerodownload.domain.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavScreen: NavKey {

    //  INITIAL         :
    @Serializable
    data object Unknown : NavScreen

    //  MAIN            :
    @Serializable
    data object Home : NavScreen

    @Serializable
    data object AppSettings : NavScreen

    //  SETTINGS        :
    @Serializable
    data object GeneralSetting : NavScreen

    @Serializable
    data object DownloaderSetting : NavScreen

    @Serializable
    data object DownloadsSetting : NavScreen
}