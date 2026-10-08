package io.bashpsk.zerodownload.core.domain.repositories

import android.app.Activity
import io.bashpsk.zerodownload.core.model.about.AppVersion
import kotlinx.coroutines.flow.Flow

interface EmptyAbout {

    fun getAppVersion(): Flow<AppVersion>

    fun setAppOpenGooglePlay(activity: Activity, appPackage: String)

    fun setLinkOpenBrowser(activity: Activity, link: String)
}