package io.bashpsk.zerodownload.data.repositories

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.data.worker.CopyWorker
import io.bashpsk.zerodownload.data.worker.DeleteWorker
import io.bashpsk.zerodownload.data.worker.MoveWorker
import io.bashpsk.zerodownload.data.worker.YtDlCommandWorker
import io.bashpsk.zerodownload.data.worker.YtDlUpdateWorker
import io.bashpsk.zerodownload.domain.repositories.EmptyWorker
import io.bashpsk.zerodownload.domain.resources.ConstantKey
import io.bashpsk.zerodownload.domain.worker.WorkRequestResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class EmptyWorkerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyWorker {

    private val workManager = WorkManager.getInstance(context = context)

    override fun getWorkInfoList(workerId: String): Flow<List<WorkInfo>> {

        return workManager.getWorkInfosForUniqueWorkFlow(
            uniqueWorkName = workerId
        ).flowOn(context = Dispatchers.IO)
    }

    override fun setYtDlCommand(command: String, title: String): Flow<WorkRequestResult> {

        return flow {

            emit(value = WorkRequestResult.Init)

            val workInputData = workDataOf(
                ConstantKey.WORK_INPUT_COMMAND to command,
                ConstantKey.WORK_INPUT_TITLE to title
            )

            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<YtDlCommandWorker>()
                .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(inputData = workInputData)
                .build()

            workManager.enqueueUniqueWork(
                uniqueWorkName = YtDlCommandWorker.WorkerId,
                existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
                request = oneTimeWorkRequest
            )

            emit(value = WorkRequestResult.Init)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setYtDlUpdate(): Flow<WorkRequestResult> {

        return flow {

            emit(value = WorkRequestResult.Init)

            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<YtDlUpdateWorker>()
                .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            workManager.enqueueUniqueWork(
                uniqueWorkName = YtDlUpdateWorker.WORKER_ID,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = oneTimeWorkRequest
            )

            emit(value = WorkRequestResult.Init)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setFileCopy(
        destination: String,
        pathList: ImmutableList<String>
    ): Flow<WorkRequestResult> {

        return flow {

            emit(value = WorkRequestResult.Init)

            pathList.forEach { path ->

                val workInputData = workDataOf(
                    ConstantKey.WORK_INPUT_DESTINATION to destination,
                    ConstantKey.WORK_INPUT_SOURCE to path
                )

                val oneTimeWorkRequest = OneTimeWorkRequestBuilder<CopyWorker>()
                    .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(inputData = workInputData)
                    .build()

                workManager.enqueueUniqueWork(
                    uniqueWorkName = CopyWorker.WORKER_ID,
                    existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
                    request = oneTimeWorkRequest
                )
            }

            emit(value = WorkRequestResult.Completed)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setFileMove(
        destination: String,
        pathList: ImmutableList<String>
    ): Flow<WorkRequestResult> {

        return flow {

            emit(value = WorkRequestResult.Init)

            pathList.forEach { path ->

                val workInputData = workDataOf(
                    ConstantKey.WORK_INPUT_DESTINATION to destination,
                    ConstantKey.WORK_INPUT_SOURCE to path
                )

                val oneTimeWorkRequest = OneTimeWorkRequestBuilder<MoveWorker>()
                    .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(inputData = workInputData)
                    .build()

                workManager.enqueueUniqueWork(
                    uniqueWorkName = MoveWorker.WORKER_ID,
                    existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
                    request = oneTimeWorkRequest
                )
            }

            emit(value = WorkRequestResult.Completed)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setFileDelete(pathList: ImmutableList<String>): Flow<WorkRequestResult> {

        return flow {

            emit(value = WorkRequestResult.Init)

            pathList.forEach { path ->

                val workInputData = workDataOf(ConstantKey.WORK_INPUT_SOURCE to path)

                val oneTimeWorkRequest = OneTimeWorkRequestBuilder<DeleteWorker>()
                    .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(inputData = workInputData)
                    .build()

                workManager.enqueueUniqueWork(
                    uniqueWorkName = DeleteWorker.WORKER_ID,
                    existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
                    request = oneTimeWorkRequest
                )
            }

            emit(value = WorkRequestResult.Init)
        }.flowOn(context = Dispatchers.IO)
    }

    override fun cancelUniqueWork(workId: String) {

        workManager.cancelUniqueWork(uniqueWorkName = workId)
    }

    override fun cancelWorkByUuid(workId: UUID) {

        workManager.cancelWorkById(id = workId)
    }
}