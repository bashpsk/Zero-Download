package io.bashpsk.zerodownload.ytdlext.extension

import com.yausername.youtubedl_android.mapper.VideoFormat
import io.bashpsk.emptylibs.formatter.format.toFileSize

fun VideoFormat.hasVideoAndAudio(): Boolean {

    return hasVideoOnly() && hasAudioOnly()
}

fun VideoFormat.hasVideoOnly(): Boolean {

    return vcodec != "none" && vcodec != null
}

fun VideoFormat.hasAudioOnly(): Boolean {

    return acodec != "none" && acodec != null
}

val VideoFormat.fileLength: Long
    get() = fileSize.takeIf { size -> size > 0L } ?: fileSizeApproximate

fun VideoFormat.toFormattedFileSize(): String {

    return fileLength.toFileSize()
}