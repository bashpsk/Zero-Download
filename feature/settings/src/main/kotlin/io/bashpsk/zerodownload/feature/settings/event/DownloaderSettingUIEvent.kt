package io.bashpsk.zerodownload.feature.settings.event

import androidx.compose.runtime.Stable

@Stable
sealed interface DownloaderSettingUIEvent {

    data object DoNothing : DownloaderSettingUIEvent

    data object UpdateYtDl : DownloaderSettingUIEvent
}