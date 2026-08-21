package io.bashpsk.zerodownload.core.navigation.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
@Parcelize
@Serializable
sealed interface NavInputData : Parcelable {

    @Serializable
    data class Media(val link: String) : NavInputData

    @Serializable
    data class Playlist(val link: String) : NavInputData

    @Serializable
    data class File(val path: String) : NavInputData
}