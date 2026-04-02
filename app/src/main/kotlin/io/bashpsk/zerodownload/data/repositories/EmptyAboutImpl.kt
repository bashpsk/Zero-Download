package io.bashpsk.zerodownload.data.repositories

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.domain.about.AppVersion
import io.bashpsk.zerodownload.domain.repositories.EmptyAbout
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmptyAboutImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyAbout {

    private val emptyScope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun getAppVersion(): Flow<AppVersion> {

        return flow {

            try {

                val packageManager = context.packageManager
                val packageName = context.packageName

                val packageInfo = when {

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {

                        packageManager.getPackageInfo(
                            packageName,
                            PackageManager.PackageInfoFlags.of(0)
                        )
                    }

                    else -> packageManager.getPackageInfo(packageName, 0)
                }

                val newAppVersion = AppVersion(
                    name = packageInfo.versionName ?: "",
                    number = PackageInfoCompat.getLongVersionCode(packageInfo),
                )

                emit(value = newAppVersion)
            } catch (exception: Exception) {

                currentCoroutineContext().ensureActive()
                Log.e(LOG_TAG, exception.message, exception)
                emit(value = AppVersion())
            }
        }.flowOn(context = Dispatchers.IO)
    }

    override fun setAppLinkShare(activity: Activity, appPackage: String, message: String) {

        emptyScope.launch(context = Dispatchers.IO + exceptionHandler) {

            val appLink = "${message}https://play.google.com/store/apps/details?id=$appPackage"

            Intent(Intent.ACTION_SEND).apply {

                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, appLink)
            }.let { intent ->

                activity.startActivity(Intent.createChooser(intent, "App Share with..."))
            }
        }
    }

    override fun setAppOpenGooglePlay(activity: Activity, appPackage: String) {

        try {

            Intent(Intent.ACTION_VIEW, "market://details?id=$appPackage".toUri()).apply {

                setPackage("com.android.vending")
            }.also(block = activity::startActivity)
        } catch (_: ActivityNotFoundException) {

            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$appPackage".toUri()
            ).also(block = activity::startActivity)
        } catch (exception: Exception) {

            Log.e(LOG_TAG, exception.message, exception)
        }
    }

    override fun setLinkOpenBrowser(activity: Activity, link: String) {

        emptyScope.launch(context = Dispatchers.IO + exceptionHandler) {

            Intent(Intent.ACTION_VIEW, link.toUri()).let { intent ->

                activity.startActivity(Intent.createChooser(intent, "Open link with..."))
            }
        }
    }

    override fun setSendEmail(activity: Activity, email: String, subject: String, body: String) {

        emptyScope.launch(context = Dispatchers.IO + exceptionHandler) {

            Intent(Intent.ACTION_SENDTO).apply {

                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }.also(block = activity::startActivity)
        }
    }
}