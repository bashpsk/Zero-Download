package io.bashpsk.zerodownload.feature.navigation.event

import androidx.compose.runtime.Stable

@Stable
sealed interface MainUIEvent {

    data object DoNothing : MainUIEvent

    data class SetNavChannel(val uri: String, val type: String) : MainUIEvent
}