package io.bashpsk.zerodownload.core.domain.worker

sealed interface WorkRequestResult {

    data object Init : WorkRequestResult

    data object Completed : WorkRequestResult
}