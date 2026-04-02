package io.bashpsk.zerodownload.domain.media

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Immutable
@Parcelize
@Serializable
data class MediaData(
    val uuid: String = Uuid.random().toString(),
    val link: String = "",
    val title: String = "",
    val artwork: String = "",
    val duration: Int = 0,
    val mediaFormats: ImmutableList<MediaFormatData.VideoAndAudio> = persistentListOf(),
    val audioFormats: ImmutableList<MediaFormatData.AudioOnly> = persistentListOf(),
    val videoFormats: ImmutableList<MediaFormatData.VideoOnly> = persistentListOf(),
    val viewCount: Long = 0L,
    val likeCount: Long = 0L,
    val dislikeCount: Long = 0L,
    val repostCount: Long = 0L,
    val channelId: String = "",
    val channelName: String = "",
    val uploadDate: String = "",
    val description: String = "",
    val extractor: String = "",
    val extractorKey: String = "",
    val index: Int = 0
) : Parcelable