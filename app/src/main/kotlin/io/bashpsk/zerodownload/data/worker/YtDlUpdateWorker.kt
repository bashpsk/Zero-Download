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
import com.yausername.youtubedl_android.YoutubeDL
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.datastore.PreferenceData
import io.bashpsk.zerodownload.domain.notification.AppNotification
import io.bashpsk.zerodownload.domain.repositories.EmptyDatastore
import io.bashpsk.zerodownload.domain.repositories.EmptyNotification
import io.bashpsk.zerodownload.domain.resources.ConstantIntent
import io.bashpsk.zerodownload.domain.resources.ConstantKey
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import io.bashpsk.zerodownload.domain.worker.WorkTaskType
import io.bashpsk.zerodownload.presentation.activities.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@HiltWorker
class YtDlUpdateWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val emptyNotification: EmptyNotification,
    private val emptyDatastore: EmptyDatastore
) : CoroutineWorker(appContext = context, params = workerParameters) {

    companion object {

        private const val NOTIFICATION_ID = 1
        const val WORKER_ID = "YT-DL-UPDATE-WORKER"
    }

    private val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)

    private val activityIntent = Intent(context, MainActivity::class.java).apply {

        action = Intent.ACTION_VIEW
        data = "${ConstantIntent.WORKER_BASE}/${WorkTaskType.LIBRARY_UPDATE.id}".toUri()
    }

    private val activityPendingIntent = TaskStackBuilder.create(context).run {

        addNextIntentWithParentStack(activityIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val notificationBuilder = emptyNotification
        .notificationBuilder(channelId = AppNotification.Channel.Application.id)
        .setSmallIcon(R.drawable.ic_system_update)
        .setContentTitle("Processing...")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .addAction(R.drawable.ic_close, "CANCEL", cancelIntent)
        .setContentIntent(activityPendingIntent)

    override suspend fun doWork(): Result = withContext(context = Dispatchers.IO) {

        return@withContext try {

            notificationBuilder.setContentTitle("Updating Library...").setProgress(100, 0, true)

            emptyNotification.setNotification(
                id = NOTIFICATION_ID,
                notification = notificationBuilder.build()
            )

            val updateOutput = YoutubeDL.getInstance().updateYoutubeDL(
                appContext = context,
                updateChannel = YoutubeDL.UpdateChannel.STABLE
            )?.name

            val workCompletedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to "Library Update Completed",
                ConstantKey.WORK_OUTPUT_MESSAGE to updateOutput
            )

            val completedNotification = emptyNotification
                .notificationBuilder(channelId = AppNotification.Channel.Application.id)
                .setSmallIcon(R.drawable.ic_download_done)
                .setContentTitle("Library Update Completed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            emptyNotification.setNotification(
                id = NOTIFICATION_ID,
                notification = completedNotification.build()
            )

            Result.success(workCompletedData)
        } catch (exception: Exception) {

            val failedMessage = "Error: ${exception.message}"

            val workFailedData = workDataOf(
                ConstantKey.WORK_OUTPUT_TITLE to "Library Update Failed",
                ConstantKey.WORK_OUTPUT_MESSAGE to failedMessage
            )

            val failedNotification = emptyNotification
                .notificationBuilder(channelId = AppNotification.Channel.Application.id)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle("Library Update Failed")
                .setStyle(NotificationCompat.BigTextStyle().bigText(failedMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            emptyNotification.setNotification(
                id = NOTIFICATION_ID,
                notification = failedNotification.build()
            )

            ensureActive()
            Log.e(LOG_TAG, exception.message, exception)
            Result.failure(workFailedData)
        } finally {

            YoutubeDL.getInstance().version(appContext = context)?.let { libVersion ->

                emptyDatastore.setPreference(
                    key = PreferenceData.YtDlLibrary.key,
                    value = libVersion
                )
            }
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