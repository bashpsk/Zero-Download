package io.bashpsk.zerodownload.domain.media

import androidx.annotation.StringRes
import io.bashpsk.zerodownload.R

enum class AudioQualityType(@param:StringRes val label: Int, val bitrate: String) {

    _32Kbps(label = R.string.kbps_32, bitrate = "32"),
    _64Kbps(label = R.string.kbps_64, bitrate = "64"),
    _128Kbps(label = R.string.kbps_128, bitrate = "128"),
    _192Kbps(label = R.string.kbps_192, bitrate = "192"),
    _256Kbps(label = R.string.kbps_256, bitrate = "256"),
    _320Kbps(label = R.string.kbps_320, bitrate = "320"),
    Best(label = R.string.best, bitrate = "0");
}