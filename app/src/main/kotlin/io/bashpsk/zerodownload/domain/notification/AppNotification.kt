package io.bashpsk.zerodownload.domain.notification

import android.app.NotificationManager
import io.bashpsk.zerodownload.domain.resources.ConstantString

object AppNotification {

    enum class Channel(val id: String, val label: String, val desc: String, val importance: Int) {

        Application(
            id = "EDM",
            label = ConstantString.APP_NAME,
            desc = "Show App & File Related Operation & Information.",
            importance = NotificationManager.IMPORTANCE_DEFAULT
        );
    }
}