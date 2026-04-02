package io.bashpsk.zerodownload.domain.worker

import androidx.compose.runtime.Stable

@Stable
sealed interface RenameWorkState {

    data class Enqueued(val title: String, val message: String) : RenameWorkState

    data class Failed(val title: String, val message: String) : RenameWorkState

    data class Running(
        val title: String,
        val newTitle: String,
        val message: String,
        val source: String
    ) : RenameWorkState

    data class Succeeded(
        val title: String,
        val newTitle: String,
        val message: String,
        val source: String
    ) : RenameWorkState
}