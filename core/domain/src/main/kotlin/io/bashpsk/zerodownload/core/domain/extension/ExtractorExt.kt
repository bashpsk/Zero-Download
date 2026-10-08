package io.bashpsk.zerodownload.core.domain.extension

import androidx.compose.runtime.Stable
import io.bashpsk.zerodownload.core.domain.R
import io.bashpsk.zerodownload.core.model.extract.ExtractorType

@Stable
fun ExtractorType.getIcon(): Int {

    return when (this) {

        ExtractorType.Youtube -> R.drawable.logo_youtube
        ExtractorType.Unknown -> R.drawable.ic_info
    }
}