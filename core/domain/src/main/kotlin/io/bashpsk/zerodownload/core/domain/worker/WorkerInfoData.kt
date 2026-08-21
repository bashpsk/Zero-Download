package io.bashpsk.zerodownload.core.domain.worker

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class WorkerInfoData<T>(val id: UUID?, val state: T)