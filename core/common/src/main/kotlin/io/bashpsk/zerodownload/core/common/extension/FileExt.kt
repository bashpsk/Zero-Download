package io.bashpsk.zerodownload.core.common.extension

import android.system.Os
import android.util.Log
import io.bashpsk.zerodownload.core.common.log.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun hasAtomicPath(sourcePath: String?, destinationPath: String?): Boolean {

    return withContext(context = Dispatchers.IO) {

        if (sourcePath == null || destinationPath == null) return@withContext false

        try {

            Os.stat(sourcePath).st_dev == Os.stat(destinationPath).st_dev
        } catch (exception: Exception) {

            Log.i(LOG_TAG, exception.message,exception)
            false
        }
    }
}