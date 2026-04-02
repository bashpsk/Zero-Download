package io.bashpsk.zerodownload.presentation.utils

import android.content.Context
import android.widget.Toast

fun Context.toastShort(message: String) {

    this.toastCustomDuration(message = message, duration = Toast.LENGTH_SHORT)
}

fun Context.toastLong(message: String) {

    this.toastCustomDuration(message = message, duration = Toast.LENGTH_LONG)
}

fun Context.toastCustomDuration(message: String, duration: Int) {

    Toast.makeText(this, message, duration).show()
}