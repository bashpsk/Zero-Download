package io.bashpsk.zerodownload.core.domain.repositories

import android.app.Activity
import io.bashpsk.zerodownload.core.model.about.AppVersion
import kotlinx.coroutines.flow.Flow

interface EmptyAbout {

    fun getAppVersion(): Flow<AppVersion>

    fun setAppLinkShare(activity: Activity, appPackage: String, message: String)

    fun setAppOpenGooglePlay(activity: Activity, appPackage: String)

    fun setLinkOpenBrowser(activity: Activity, link: String)

    fun setSendEmail(activity: Activity, email: String, subject: String, body: String)
}