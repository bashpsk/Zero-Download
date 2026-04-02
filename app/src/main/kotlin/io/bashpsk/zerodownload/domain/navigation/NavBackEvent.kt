package io.bashpsk.zerodownload.domain.navigation

sealed interface NavBackEvent {

    data object Running : NavBackEvent

    data object Completed : NavBackEvent
}