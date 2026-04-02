package io.bashpsk.zerodownload.domain.worker

import androidx.compose.runtime.Stable

@Stable
sealed interface MoveWorkState {

    data class Enqueued(val title: String, val message: String) : MoveWorkState

    data class Failed(val title: String, val message: String) : MoveWorkState

    data class Running(
        val title: String,
        val message: String,
        val source: String,
        val destination: String,
        val total: Long,
        val saved: Long
    ) : MoveWorkState

    data class Succeeded(
        val title: String,
        val message: String,
        val source: String,
        val destination: String,
        val total: Long
    ) : MoveWorkState
}