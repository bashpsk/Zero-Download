package io.bashpsk.zerodownload.domain.worker

sealed interface WorkRequestResult {

    data object Init : WorkRequestResult

    data object Completed : WorkRequestResult
}