package io.bashpsk.zerodownload.domain.events

sealed interface MainUIEvent {

    data object DoNothing : MainUIEvent

    data class SetNavChannel(val uri: String, val type: String) : MainUIEvent
}