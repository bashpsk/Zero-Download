package io.bashpsk.zerodownload

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.yausername.aria2c.Aria2c
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.HiltAndroidApp
import io.bashpsk.zerodownload.domain.repositories.EmptyNotification
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    @Inject
    lateinit var emptyNotification: EmptyNotification

    private val appScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Default)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun onCreate() {
        super.onCreate()

        appScope.launch(context = Dispatchers.IO + exceptionHandler) {

            emptyNotification.setNotificationChannels()
            YoutubeDL.getInstance().init(appContext = this@MainApplication)
            FFmpeg.getInstance().init(appContext = this@MainApplication)
            Aria2c.getInstance().init(appContext = this@MainApplication)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build()
}