package io.bashpsk.zerodownload.core.navigation.screen

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

    @Serializable
    data object Settings : NavScreen

    @Serializable
    data object Downloads : NavScreen

    @Serializable
    data object About : NavScreen

    @Serializable
    data object Command : NavScreen

    //  SETTINGS        :
    @Serializable
    data object GeneralSetting : NavScreen

    @Serializable
    data object DownloaderSetting : NavScreen

    @Serializable
    data object DownloadsSetting : NavScreen
}