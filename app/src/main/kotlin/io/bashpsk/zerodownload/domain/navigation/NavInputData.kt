package io.bashpsk.zerodownload.domain.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import io.bashpsk.zerodownload.domain.file.FileCopyType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
@Parcelize
@Serializable
data class NavPathInput(val paths: List<String> = emptyList()) : Parcelable

@Immutable
@Parcelize
@Serializable
data class NavCopyInput(
    val type: FileCopyType = FileCopyType.INIT,
    val paths: List<String> = emptyList()
) : Parcelable