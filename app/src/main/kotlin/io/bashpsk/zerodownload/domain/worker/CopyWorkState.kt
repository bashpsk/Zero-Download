package io.bashpsk.zerodownload.domain.worker

import androidx.compose.runtime.Stable

@Stable
sealed interface CopyWorkState {

    data class Enqueued(val title: String, val message: String) : CopyWorkState

    data class Failed(val title: String, val message: String) : CopyWorkState

    data class Running(
        val title: String,
        val message: String,
        val source: String,
        val destination: String,
        val total: Long,
        val saved: Long
    ) : CopyWorkState

    data class Succeeded(
        val title: String,
        val message: String,
        val source: String,
        val destination: String,
        val total: Long
    ) : CopyWorkState
}