package io.bashpsk.zerodownload.domain.events

sealed interface DownloaderSettingUIEvent {

    data object DoNothing : DownloaderSettingUIEvent

    data object UpdateYtDl : DownloaderSettingUIEvent
}