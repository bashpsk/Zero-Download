package io.bashpsk.zerodownload

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import io.bashpsk.zerodownload.core.common.log.LOG_TAG
import io.bashpsk.zerodownload.core.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.core.domain.repositories.EmptyNotification
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

    @Inject
    lateinit var emptyMedia: EmptyMedia

    private val appScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Default)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Log.e(LOG_TAG, throwable.message, throwable)
    }

    override fun onCreate() {
        super.onCreate()

        appScope.launch(context = Dispatchers.IO + exceptionHandler) {

            emptyNotification.setNotificationChannels()
            emptyMedia.setInitYtDl()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build()
}