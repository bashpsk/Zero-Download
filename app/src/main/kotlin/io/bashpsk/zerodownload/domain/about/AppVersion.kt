package io.bashpsk.zerodownload.domain.about

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
@Parcelize
@Serializable
data class AppVersion(val name: String = "", val number: Long = 0L) : Parcelable