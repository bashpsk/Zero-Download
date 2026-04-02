package io.bashpsk.zerodownload.domain.media

sealed interface MediaExtensionType {

    val ext: String

    fun toLabel(): String {

        return ext.uppercase()
    }

    enum class Audio(override val ext: String) : MediaExtensionType {

        MP3(ext = "mp3"),
        M4A(ext = "m4a"),
        WAV(ext = "wav"),
        FLAC(ext = "flac");
    }

    enum class Video(override val ext: String) : MediaExtensionType {

        MP4(ext = "mp4"),
        MKV(ext = "mkv"),
        WEBM(ext = "webm"),
        MOV(ext = "mov"),
        AVI(ext = "avi"),
        FLV(ext = "flv");
    }
}