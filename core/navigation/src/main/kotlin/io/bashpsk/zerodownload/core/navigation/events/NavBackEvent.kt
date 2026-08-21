package io.bashpsk.zerodownload.core.navigation.events

sealed interface NavBackEvent {

    data object Running : NavBackEvent

    data object Completed : NavBackEvent
}