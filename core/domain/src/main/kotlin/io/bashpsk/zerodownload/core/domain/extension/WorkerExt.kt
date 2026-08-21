package io.bashpsk.zerodownload.core.domain.extension

import io.bashpsk.zerodownload.core.domain.worker.WorkRequestResult
import io.bashpsk.zerodownload.core.navigation.events.NavBackEvent

inline val WorkRequestResult.toNavBackEvent: NavBackEvent
    get() = when (this) {

        is WorkRequestResult.Init -> NavBackEvent.Running
        is WorkRequestResult.Completed -> NavBackEvent.Completed
    }