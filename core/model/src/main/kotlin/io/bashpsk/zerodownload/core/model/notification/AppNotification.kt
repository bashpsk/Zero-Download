package io.bashpsk.zerodownload.core.model.notification

import android.app.NotificationManager
import io.bashpsk.zerodownload.core.model.resources.ConstantString

object AppNotification {

    enum class Channel(val id: String, val label: String, val desc: String, val importance: Int) {

        Application(
            id = "Zero Download",
            label = ConstantString.APP_NAME,
            desc = "Show App & File Related Operation & Information.",
            importance = NotificationManager.IMPORTANCE_DEFAULT
        );
    }
}