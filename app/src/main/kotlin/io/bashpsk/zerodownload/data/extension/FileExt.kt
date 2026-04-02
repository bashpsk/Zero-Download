package io.bashpsk.zerodownload.data.extension

import android.graphics.Bitmap
import android.system.Os
import android.util.Log
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
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

fun findCompressFormatFromExtension(extension: String): Bitmap.CompressFormat {

    return when (extension.lowercase()) {

        "png" -> Bitmap.CompressFormat.PNG
        "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG
        "webp" -> Bitmap.CompressFormat.WEBP
        else -> Bitmap.CompressFormat.JPEG
    }
}