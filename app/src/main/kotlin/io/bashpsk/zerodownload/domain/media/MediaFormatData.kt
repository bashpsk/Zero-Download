package io.bashpsk.zerodownload.domain.media

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Stable
@Parcelize
@Serializable
sealed interface MediaFormatData : Parcelable {

    val uuid: String
    val link: String
    val url: String
    val formatId: String
    val format: String
    val formatNote: String
    val formatLabel: String
    val qualityLabel: String
    val fileSize: String
    val codec: String
    val ext: String

    @Serializable
    data class VideoAndAudio(
        override val uuid: String = Uuid.random().toString(),
        override val link: String = "",
        override val url: String = "",
        override val formatId: String = "",
        override val format: String = "",
        override val formatNote: String = "",
        override val formatLabel: String = "",
        override val qualityLabel: String = "",
        override val fileSize: String = "",
        override val codec: String = "",
        override val ext: String = "",
        val width: Int = 0,
        val height: Int = 0,
        val fps: Int = 0,
        val abr: Int = 0,
        val tbr: Int = 0,
        val asr: Int = 0
    ) : MediaFormatData

    @Serializable
    data class VideoOnly(
        override val uuid: String = Uuid.random().toString(),
        override val link: String = "",
        override val url: String = "",
        override val formatId: String = "",
        override val format: String = "",
        override val formatNote: String = "",
        override val formatLabel: String = "",
        override val qualityLabel: String = "",
        override val fileSize: String = "",
        override val codec: String = "",
        override val ext: String = "",
        val width: Int = 0,
        val height: Int = 0,
        val fps: Int = 0
    ) : MediaFormatData

    @Serializable
    data class AudioOnly(
        override val uuid: String = Uuid.random().toString(),
        override val link: String = "",
        override val url: String = "",
        override val formatId: String = "",
        override val format: String = "",
        override val formatNote: String = "",
        override val formatLabel: String = "",
        override val qualityLabel: String = "",
        override val fileSize: String = "",
        override val codec: String = "",
        override val ext: String = "",
        val abr: Int = 0,
        val tbr: Int = 0,
        val asr: Int = 0
    ) : MediaFormatData

    @Serializable
    data class Unknown(
        override val uuid: String = Uuid.random().toString(),
        override val link: String = "",
        override val url: String = "",
        override val formatId: String = "",
        override val format: String = "",
        override val formatNote: String = "",
        override val formatLabel: String = "",
        override val qualityLabel: String = "",
        override val fileSize: String = "",
        override val codec: String = "",
        override val ext: String = ""
    ) : MediaFormatData
}