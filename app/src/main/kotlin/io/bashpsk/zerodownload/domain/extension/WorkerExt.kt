package io.bashpsk.zerodownload.domain.extension

import io.bashpsk.zerodownload.domain.navigation.NavBackEvent
import io.bashpsk.zerodownload.domain.worker.WorkRequestResult

inline val WorkRequestResult.toNavBackEvent: NavBackEvent
    get() = when (this) {

        is WorkRequestResult.Init -> NavBackEvent.Running
        is WorkRequestResult.Completed -> NavBackEvent.Completed
    }