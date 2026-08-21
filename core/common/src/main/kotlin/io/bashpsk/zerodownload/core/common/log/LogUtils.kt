package io.bashpsk.zerodownload.core.common.log

import android.util.Log

const val LOG_TAG = "ZERO-DOWNLOAD"

fun String.setDebug() {

    Log.d("PSK", this)
}