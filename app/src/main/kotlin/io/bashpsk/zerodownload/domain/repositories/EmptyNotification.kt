package io.bashpsk.zerodownload.domain.repositories

import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import io.bashpsk.zerodownload.domain.notification.AppNotification

interface EmptyNotification {

    fun createNotificationChannel(channel: AppNotification.Channel)

    fun notificationBuilder(channelId: String): NotificationCompat.Builder

    fun setNotification(id: Int, notification: Notification)

    fun cancelNotification(id: Int)

    fun setNotificationChannels()

    fun createForegroundInfo(
        channel: AppNotification.Channel,
        id: Int,
        notification: Notification
    ): ForegroundInfo
}