package io.bashpsk.zerodownload.domain.worker

import androidx.compose.runtime.Stable

@Stable
sealed interface DeleteWorkState {

    data class Enqueued(val title: String, val message: String) : DeleteWorkState

    data class Failed(val title: String, val message: String) : DeleteWorkState

    data class Running(
        val title: String,
        val message: String,
        val source: String,
        val total: Long
    ) : DeleteWorkState

    data class Succeeded(
        val title: String,
        val message: String,
        val source: String,
        val total: Long
    ) : DeleteWorkState
}