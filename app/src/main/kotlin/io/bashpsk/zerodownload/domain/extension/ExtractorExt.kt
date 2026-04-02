package io.bashpsk.zerodownload.domain.extension

import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.ytdlext.extract.ExtractorType

fun ExtractorType.getIcon(): Int {

    return when (this) {

        ExtractorType.Youtube -> R.drawable.ic_info
        ExtractorType.Unknown -> R.drawable.ic_info
    }
}