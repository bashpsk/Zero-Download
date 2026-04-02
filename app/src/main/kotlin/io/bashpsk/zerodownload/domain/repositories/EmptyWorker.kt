package io.bashpsk.zerodownload.domain.repositories

import androidx.work.WorkInfo
import io.bashpsk.zerodownload.domain.worker.WorkRequestResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface EmptyWorker {

    fun getWorkInfoList(workerId: String): Flow<List<WorkInfo>>

    fun setYtDlCommand(command: String, title: String): Flow<WorkRequestResult>

    fun setYtDlUpdate(): Flow<WorkRequestResult>

    fun setFileCopy(
        destination: String,
        pathList: ImmutableList<String>
    ): Flow<WorkRequestResult>

    fun setFileMove(
        destination: String,
        pathList: ImmutableList<String>
    ): Flow<WorkRequestResult>

    fun setFileDelete(pathList: ImmutableList<String>): Flow<WorkRequestResult>

    fun cancelUniqueWork(workId: String)

    fun cancelWorkByUuid(workId: UUID)
}