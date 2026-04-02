package io.bashpsk.zerodownload.data.repositories

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.domain.notification.AppNotification
import io.bashpsk.zerodownload.domain.repositories.EmptyNotification
import javax.inject.Inject

@SuppressLint("MissingPermission")
class EmptyNotificationImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyNotification {

    private val notificationManager = NotificationManagerCompat.from(context)

    override fun createNotificationChannel(channel: AppNotification.Channel) {

        NotificationChannel(channel.id, channel.label, channel.importance).apply {

            description = channel.desc
        }.let(block = notificationManager::createNotificationChannel)
    }

    override fun notificationBuilder(channelId: String): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, channelId)
    }

    override fun setNotification(id: Int, notification: Notification) {

        notificationManager.notify(id, notification)
    }

    override fun cancelNotification(id: Int) {

        notificationManager.cancel(id)
    }

    override fun setNotificationChannels() {

        AppNotification.Channel.entries.forEach { channel ->

            createNotificationChannel(channel = channel)
        }
    }

    override fun createForegroundInfo(
        channel: AppNotification.Channel,
        id: Int,
        notification: Notification
    ): ForegroundInfo {

        createNotificationChannel(channel = channel)

        return when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ForegroundInfo(
                id,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )

            else -> ForegroundInfo(id, notification)
        }
    }
}