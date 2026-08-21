package io.bashpsk.zerodownload.core.domain.extension

import io.bashpsk.zerodownload.core.domain.R
import io.bashpsk.zerodownload.core.model.extract.ExtractorType

fun ExtractorType.getIcon(): Int {

    return when (this) {

        ExtractorType.Youtube -> R.drawable.ic_info
        ExtractorType.Unknown -> R.drawable.ic_info
    }
}