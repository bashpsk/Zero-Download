package io.bashpsk.zerodownload.domain.media

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
@Parcelize
@Serializable
data class PlaylistMediaData(
    val link: String = "",
    val title: String = "",
    val mediaList: ImmutableList<MediaData> = persistentListOf(),
    val description: String = "",
    val channelId: String = "",
    val channelName: String = "",
    val extractor: String = "",
    val extractorKey: String = ""
) : Parcelable