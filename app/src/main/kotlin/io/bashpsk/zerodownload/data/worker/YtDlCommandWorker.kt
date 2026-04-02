package io.bashpsk.zerodownload.data.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Environment
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
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.notification.AppNotification
import io.bashpsk.zerodownload.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.domain.repositories.EmptyNotification
import io.bashpsk.zerodownload.domain.resources.ConstantCommand
import io.bashpsk.zerodownload.domain.resources.ConstantIntent
import io.bashpsk.zerodownload.domain.resources.ConstantKey
import io.bashpsk.zerodownload.domain.resources.ConstantString
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import io.bashpsk.zerodownload.domain.worker.WorkTaskType
import io.bashpsk.zerodownload.presentation.activities.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Collections
import java.util.regex.Pattern

@HiltWorker
class YtDlCommandWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val emptyNotification: EmptyNotification,
    private val emptyMedia: EmptyMedia
) : CoroutineWorker(appContext = context, params = workerParameters) {

    companion object {

        val WorkerId = WorkTaskType.YtDlCommand.uuid
         val NotificationId = WorkTaskType.YtDlCommand.id
    }

    val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)

    val activityIntent = Intent(context, MainActivity::class.java).apply {

        action = Intent.ACTION_VIEW
        data = "${ConstantIntent.WORKER_BASE}/${WorkTaskType.YtDlCommand.id}".toUri()
    }

    val activityPendingIntent = TaskStackBuilder.create(context).run {

        addNextIntentWithParentStack(activityIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val notificationBuilder = emptyNotification
        .notificationBuilder(channelId = AppNotification.Channel.Application.id)
        .setSmallIcon(R.drawable.ic_download)
        .setContentTitle("Processing...")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .addAction(R.drawable.ic_close, "CANCEL", cancelIntent)
        .setContentIntent(activityPendingIntent)

    override suspend fun doWork() : Result = withContext(context = Dispatchers.IO) {

        setForeground(foregroundInfo = createForegroundInfo())

        val workInitData = workDataOf(ConstantKey.WORK_OUTPUT_MESSAGE to "Input Data is Empty.")

        val titleInput = inputData.getString(
            ConstantKey.WORK_INPUT_TITLE
        ) ?: return@withContext Result.failure(workInitData)

        val commandInput = inputData.getString(
            ConstantKey.WORK_INPUT_COMMAND
        ) ?: return@withContext Result.failure(workInitData)

        notificationBuilder.setProgress(100, 0, true)

        emptyNotification.setNotification(
            id = NotificationId,
            notification = notificationBuilder.build()
        )

        return@withContext try {

            val commandRegex = "\"([^\"]*)\"|(\\S+)"

            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )

            val rootDirectory = File(downloadsDirectory, ConstantString.APP_NAME)

            val dlRequest = YoutubeDLRequest(urls = Collections.emptyList())
            val commandMatcher = Pattern.compile(commandRegex).matcher(commandInput)

            when {

                !rootDirectory.exists() -> rootDirectory.mkdirs()
            }

            while (commandMatcher.find()) {

                val matchedCommand = commandMatcher.group(1).takeIf { command ->

                    command != null
                } ?: commandMatcher.group(2)

                matchedCommand?.let { matchedCommand ->

                    dlRequest.apply {

                        addOption(option = matchedCommand)

                        addOption(
                            option = ConstantCommand.DOWNLOADER_OPTION,
                            argument = ConstantCommand.DOWNLOADER_ARGS
                        )

                        addOption(
                            option = ConstantCommand.EXTERNAL_DOWNLOADER_OPTION,
                            argument = ConstantCommand.EXTERNAL_DOWNLOADER_ARGS
                        )
                    }
                }
            }

            val commandResponse = YoutubeDL.getInstance().execute(
                request = dlRequest,
                processId = commandInput,
                callback = { progress, eta, line ->

//                    "PROGRESS: $progress, ETA: $eta, LINE: $line".setDebug()

                    val progressData = workDataOf(
                        ConstantKey.WORK_OUTPUT_TITLE to titleInput,
                        ConstantKey.WORK_OUTPUT_MESSAGE to line,
                        ConstantKey.WORK_OUTPUT_ENTRY to commandInput,
                        ConstantKey.WORK_OUTPUT_PROGRESS to progress,
                        ConstantKey.WORK_OUTPUT_ETA to eta
                    )

                    notificationBuilder
                        .setContentTitle(titleInput)
                        .setSubText("$progress%")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(line))
                        .setProgress(100, progress.toInt(), progress.toInt() == 0)

                    emptyNotification.setNotification(
                        id = NotificationId,
                        notification = notificationBuilder.build()
                    )

                    CoroutineScope(context = Dispatchers.IO).launch {

                        setProgress(data = progressData)
                    }
                }
            )

            val commandOutputFile = File(context.cacheDir, "${System.currentTimeMillis()}.txt")

            BufferedOutputStream(FileOutputStream(commandOutputFile)).use { bufferedOutputStream ->

                bufferedOutputStream.write(commandResponse.out.toByteArray())
            }

            rootDirectory.listFiles()?.forEach { file ->

                file?.let { destinationFile ->

                    emptyMedia.setScanMediaPath(destinationFile.path)
                }
            }

            val completedNotification = emptyNotification
                .notificationBuilder(channelId = AppNotification.Channel.Application.id)
                .setSmallIcon(R.drawable.ic_download_done)
                .setContentTitle(titleInput)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Completed"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            emptyNotification.setNotification(
                id = commandInput.hashCode(),
                notification = completedNotification.build()
            )

            val workCompletedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to titleInput,
                ConstantKey.WORK_OUTPUT_MESSAGE to "Completed",
                ConstantKey.WORK_OUTPUT_ENTRY to commandInput,
                ConstantKey.WORK_OUTPUT_DESTINATION to commandOutputFile.path
            )

            Result.success(workCompletedData)
        } catch (exception: Exception) {

            val message = "Error: ${exception.message}"

            val completedNotification = emptyNotification
                .notificationBuilder(channelId = AppNotification.Channel.Application.id)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle(titleInput)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val completedWorkData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to titleInput,
                ConstantKey.WORK_OUTPUT_MESSAGE to message
            )

            emptyNotification.setNotification(
                id = commandInput.hashCode(),
                notification = completedNotification.build()
            )

            ensureActive()
            emptyMedia.setYtDlDestroy(id = commandInput)
            Log.e(LOG_TAG, exception.message, exception)
            Result.failure(completedWorkData)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {

        return createForegroundInfo()
    }

    private fun createForegroundInfo(): ForegroundInfo {

        return emptyNotification.createForegroundInfo(
            channel = AppNotification.Channel.Application,
            id = NotificationId,
            notification = notificationBuilder.build()
        )
    }
}