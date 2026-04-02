package io.bashpsk.zerodownload.data.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.notification.AppNotification
import io.bashpsk.zerodownload.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.domain.repositories.EmptyNotification
import io.bashpsk.zerodownload.domain.resources.ConstantIntent
import io.bashpsk.zerodownload.domain.resources.ConstantKey
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import io.bashpsk.zerodownload.domain.worker.WorkTaskType
import io.bashpsk.zerodownload.presentation.activities.main.MainActivity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class DeleteWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val emptyNotification: EmptyNotification,
    private val emptyMedia: EmptyMedia
) : CoroutineWorker(appContext = context, params = workerParameters) {

    companion object {

        private const val NOTIFICATION_ID = 5
        const val WORKER_ID = "FILE-DELETE-WORKER"
    }

    private val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)

    private val activityIntent = Intent(context, MainActivity::class.java).apply {

        action = Intent.ACTION_VIEW
        data = "${ConstantIntent.WORKER_BASE}/${WorkTaskType.FileDelete.id}".toUri()
    }

    private val activityPendingIntent = TaskStackBuilder.create(context).run {

        addNextIntentWithParentStack(activityIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val notificationBuilder = emptyNotification
        .notificationBuilder(channelId = AppNotification.Channel.Application.id)
        .setSmallIcon(R.drawable.ic_delete_forever)
        .setContentTitle("Processing...")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .addAction(R.drawable.ic_close, "CANCEL", cancelIntent)
        .setContentIntent(activityPendingIntent)

    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        val workInitData = workDataOf(ConstantKey.WORK_OUTPUT_MESSAGE to "Input Data is Empty")

        val pathInput = workerParameters.inputData.getString(
            ConstantKey.WORK_INPUT_SOURCE
        ) ?: return@withContext Result.failure(workInitData)

        notificationBuilder.setProgress(100, 0, true)

        emptyNotification.setNotification(
            id = NOTIFICATION_ID,
            notification = notificationBuilder.build()
        )

        return@withContext try {

            val sourceFile = File(pathInput)

            val workProgressData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to sourceFile.name,
                ConstantKey.WORK_OUTPUT_MESSAGE to "File Deleting",
                ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                ConstantKey.WORK_OUTPUT_TOTAL to sourceFile.length()
            )

            setProgress(data = workProgressData)

            notificationBuilder
                .setContentTitle("File Deleting")
                .setStyle(NotificationCompat.BigTextStyle().bigText(sourceFile.name))
                .setProgress(100, 0, true)

            emptyNotification.setNotification(
                id = NOTIFICATION_ID,
                notification = notificationBuilder.build()
            )

            sourceFile.deleteRecursively().let { isDeleted ->

                when (isDeleted) {

                    true -> {

                        val workScanningData = workDataOf(
                            ConstantKey.WORK_OUTPUT_TITLE to sourceFile.name,
                            ConstantKey.WORK_OUTPUT_MESSAGE to "Scanning...",
                            ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                            ConstantKey.WORK_OUTPUT_TOTAL to sourceFile.length()
                        )

                        setProgress(data = workScanningData)

                        notificationBuilder
                            .setContentTitle("Scanning...")
                            .setStyle(NotificationCompat.BigTextStyle().bigText(sourceFile.name))
                            .setProgress(100, 0, true)

                        emptyNotification.setNotification(
                            id = NOTIFICATION_ID,
                            notification = notificationBuilder.build()
                        )

                        persistentListOf(pathInput).forEach { path->

                            emptyMedia.setScanMediaPath(path = path)
                        }
                    }

                    false -> {

                        val workFailedData = workDataOf(
                            ConstantKey.WORK_OUTPUT_TITLE to sourceFile.name,
                            ConstantKey.WORK_OUTPUT_MESSAGE to "Unknown Error"
                        )

                        setProgress(data = workFailedData)

                        val failedNotification = emptyNotification
                            .notificationBuilder(AppNotification.Channel.Application.id)
                            .setSmallIcon(R.drawable.ic_info)
                            .setContentTitle("File Delete Failed")
                            .setStyle(NotificationCompat.BigTextStyle().bigText("Unknown Error"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        emptyNotification.setNotification(
                            id = pathInput.hashCode(),
                            notification = failedNotification.build()
                        )

                        Result.failure(workFailedData)
                    }
                }
            }

            val workCompletedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to sourceFile.name,
                ConstantKey.WORK_OUTPUT_MESSAGE to "Deleted",
                ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                ConstantKey.WORK_OUTPUT_TOTAL to sourceFile.length()
            )

            Result.success(workCompletedData)
        } catch (exception: Exception) {

            val title = pathInput.substringAfterLast(delimiter = File.separatorChar)
            val failedMessage = "Error - ${exception.message}"

            val workFailedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to title,
                ConstantKey.WORK_OUTPUT_MESSAGE to failedMessage
            )

            val failedNotification = emptyNotification
                .notificationBuilder(channelId = AppNotification.Channel.Application.id)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle("File Delete Failed")
                .setStyle(NotificationCompat.BigTextStyle().bigText(failedMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            emptyNotification.setNotification(
                id = pathInput.hashCode(),
                notification = failedNotification.build()
            )

            Log.e(LOG_TAG, exception.message, exception)
            Result.failure(workFailedData)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {

        return when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ForegroundInfo(
                NOTIFICATION_ID,
                notificationBuilder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )

            else -> ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}