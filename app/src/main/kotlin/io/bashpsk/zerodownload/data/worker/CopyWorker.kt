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
import io.bashpsk.emptylibs.formatter.format.findPercentage
import io.bashpsk.emptylibs.formatter.format.toFileSize
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@HiltWorker
class CopyWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val emptyNotification: EmptyNotification,
    private val emptyMedia: EmptyMedia
) : CoroutineWorker(appContext = context, params = workerParameters) {

    companion object {

        private const val NOTIFICATION_ID = 3
        const val WORKER_ID = "FILE-COPY-WORKER"
    }

    private val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)

    private val activityIntent = Intent(context, MainActivity::class.java).apply {

        action = Intent.ACTION_VIEW
        data = "${ConstantIntent.WORKER_BASE}/${WorkTaskType.FileCopy.id}".toUri()
    }

    private val activityPendingIntent = TaskStackBuilder.create(context).run {

        addNextIntentWithParentStack(activityIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val notificationBuilder = emptyNotification
        .notificationBuilder(channelId = AppNotification.Channel.Application.id)
        .setSmallIcon(R.drawable.ic_file_copy)
        .setContentTitle("Processing...")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .addAction(R.drawable.ic_close, "CANCEL", cancelIntent)
        .setContentIntent(activityPendingIntent)

    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        val workInitData = workDataOf(ConstantKey.WORK_OUTPUT_MESSAGE to "Input Data is Empty")

        val destinationInput = workerParameters.inputData.getString(
            key = ConstantKey.WORK_INPUT_DESTINATION
        ) ?: return@withContext Result.failure(workInitData)

        val pathInput = workerParameters.inputData.getString(
            key = ConstantKey.WORK_INPUT_SOURCE
        ) ?: return@withContext Result.failure(workInitData)

        var progressJob: Job? = null

        notificationBuilder.setProgress(100, 0, true)

        emptyNotification.setNotification(
            id = NOTIFICATION_ID,
            notification = notificationBuilder.build()
        )

        return@withContext try {

            val fileSystem = SystemFileSystem

            val sourcePath = Path(path = pathInput)
            val destinationPath = Path(base = destinationInput, sourcePath.name)

            val sourceFile = File(pathInput)
            val destinationFile = File(destinationInput, sourceFile.name)

            val bufferSize = DEFAULT_BUFFER_SIZE.toLong()

            destinationFile.parentFile?.mkdirs()

            progressJob = launch(context = Dispatchers.IO) {

                while (isActive) {

                    val totalBytes = sourceFile.length()
                    val savedBytes = destinationFile.length()

                    val progress = findPercentage(total = totalBytes, savedBytes)
                    val totalSize = totalBytes.toFileSize(context = context)
                    val savedSize = savedBytes.toFileSize(context = context)

                    val workProgressData = workDataOf(
                        ConstantKey.WORK_OUTPUT_TITLE to sourcePath.name,
                        ConstantKey.WORK_OUTPUT_MESSAGE to "Copying",
                        ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                        ConstantKey.WORK_OUTPUT_DESTINATION to destinationFile.path,
                        ConstantKey.WORK_OUTPUT_TOTAL to totalBytes,
                        ConstantKey.WORK_OUTPUT_SAVED to savedBytes
                    )

                    setProgress(data = workProgressData)

                    notificationBuilder
                        .setContentTitle("Copying $savedSize/$totalSize ($progress%)")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(sourcePath.name))
                        .setProgress(100, progress, progress == 0)

                    emptyNotification.setNotification(
                        id = NOTIFICATION_ID,
                        notification = notificationBuilder.build()
                    )

                    ensureActive()
                    delay(duration = 200.milliseconds)
                }
            }

            fileSystem.source(path = sourcePath).use { source ->

                fileSystem.sink(path = destinationPath).use { sink ->

                    val buffer = Buffer()

                    while (source.readAtMostTo(sink = buffer, byteCount = bufferSize) >= 0) {

                        ensureActive()
                        sink.write(source = buffer, byteCount = buffer.size)
                    }
                }
            }

            progressJob.cancel()

            val workScanningData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to destinationFile.name,
                ConstantKey.WORK_OUTPUT_MESSAGE to "Scanning",
                ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                ConstantKey.WORK_OUTPUT_DESTINATION to destinationFile.path,
                ConstantKey.WORK_OUTPUT_TOTAL to destinationFile.length(),
                ConstantKey.WORK_OUTPUT_SAVED to destinationFile.length()
            )

            setProgress(data = workScanningData)

            notificationBuilder
                .setContentTitle("Scanning...")
                .setStyle(NotificationCompat.BigTextStyle().bigText(destinationPath.name))
                .setProgress(100, 0, true)

            emptyNotification.setNotification(
                id = NOTIFICATION_ID,
                notification = notificationBuilder.build()
            )

            persistentListOf(sourceFile.path, destinationFile.path).forEach { path->

                emptyMedia.setScanMediaPath(path = path)
            }

            val workCompletedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to destinationPath.name,
                ConstantKey.WORK_OUTPUT_MESSAGE to "Copied",
                ConstantKey.WORK_OUTPUT_SOURCE to sourceFile.path,
                ConstantKey.WORK_OUTPUT_DESTINATION to destinationFile.path,
                ConstantKey.WORK_OUTPUT_TOTAL to destinationFile.length()
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
                .setContentTitle("File Copy Failed")
                .setStyle(NotificationCompat.BigTextStyle().bigText(failedMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            emptyNotification.setNotification(
                id = pathInput.hashCode(),
                notification = failedNotification.build()
            )

            progressJob?.cancel()
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