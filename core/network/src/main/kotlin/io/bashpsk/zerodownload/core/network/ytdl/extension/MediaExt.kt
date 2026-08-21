package io.bashpsk.zerodownload.core.network.ytdl.extension

import com.yausername.youtubedl_android.mapper.VideoFormat
import com.yausername.youtubedl_android.mapper.VideoInfo
import io.bashpsk.emptylibs.formatter.format.findResolutionLabel
import io.bashpsk.zerodownload.core.model.media.MediaData
import io.bashpsk.zerodownload.core.model.media.MediaFormatData
import io.bashpsk.zerodownload.core.model.media.PlaylistMediaData
import io.bashpsk.zerodownload.core.network.ytdl.mapper.PlaylistInfo
import io.bashpsk.zerodownload.core.network.ytdl.mapper.PlaylistVideoInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun VideoFormat.toMediaFormatData(): MediaFormatData {

    return when {

        hasVideoAndAudio() -> MediaFormatData.VideoAndAudio(
            url = url ?: "",
            formatId = formatId ?: "",
            format = format ?: "",
            formatNote = formatNote ?: "",
            formatLabel = formatNote?.takeIf { format ->

                format.isNotEmpty()
            }?.replaceFirstChar { char -> char.uppercaseChar() } ?: findResolutionLabel(
                width = width,
                height = height
            ),
            qualityLabel = findResolutionLabel(width = width, height = height),
            fileSize = toFormattedFileSize(),
            codec = "${vcodec ?: ""} + ${acodec ?: ""}",
            ext = ext ?: "",
            width = width,
            height = height,
            fps = fps,
            abr = abr,
            tbr = tbr,
            asr = asr
        )

        hasVideoOnly() -> MediaFormatData.VideoOnly(
            url = url ?: "",
            formatId = formatId ?: "",
            format = format ?: "",
            formatNote = formatNote ?: "",
            formatLabel = formatNote?.takeIf { format ->

                format.isNotEmpty()
            }?.replaceFirstChar { char -> char.uppercaseChar() } ?: findResolutionLabel(
                width = width,
                height = height
            ),
            qualityLabel = findResolutionLabel(width = width, height = height),
            fileSize = toFormattedFileSize(),
            codec = vcodec ?: "",
            ext = ext ?: "",
            width = width,
            height = height,
            fps = fps
        )

        hasAudioOnly() -> MediaFormatData.AudioOnly(
            url = url ?: "",
            formatId = formatId ?: "",
            format = format ?: "",
            formatNote = formatNote ?: "",
            formatLabel = formatNote?.takeIf { format ->

                format.isNotEmpty()
            }?.replaceFirstChar { char -> char.uppercaseChar() } ?: "$abr Kbps",
            qualityLabel = "$abr Kbps",
            fileSize = toFormattedFileSize(),
            codec = acodec ?: "",
            ext = ext ?: "",
            abr = abr,
            tbr = tbr,
            asr = asr
        )

        else -> MediaFormatData.Unknown(
            url = url ?: "",
            formatId = formatId ?: "",
            format = format ?: "",
            formatNote = formatNote ?: "",
            formatLabel = formatNote?.takeIf { format ->

                format.isNotEmpty()
            }?.replaceFirstChar { char -> char.uppercaseChar() } ?: "Unknown",
            qualityLabel = "Unknown",
            fileSize = toFormattedFileSize(),
            codec = acodec ?: "",
            ext = ext ?: ""
        )
    }
}

fun VideoInfo.toMediaData(): MediaData {

    return MediaData(
        link = webpageUrl ?: "",
        title = title ?: "",
        artwork = thumbnail ?: "",
        duration = duration,
        mediaFormats = persistentListOf(),
        audioFormats = persistentListOf(),
        videoFormats = persistentListOf(),
        viewCount = viewCount?.toLongOrNull() ?: 0L,
        likeCount = likeCount?.toLongOrNull() ?: 0L,
        dislikeCount = dislikeCount?.toLongOrNull() ?: 0L,
        repostCount = repostCount?.toLongOrNull() ?: 0L,
        channelId = uploaderId ?: "",
        channelName = uploader ?: "",
        uploadDate = uploadDate ?: "",
        description = description ?: "",
        extractor = extractor ?: "",
        extractorKey = extractorKey ?: "",
        index =  0
    )
}

fun PlaylistVideoInfo.toMediaData(): MediaData {

    return MediaData(
        link = webpageUrl ?: "",
        title = title ?: "",
        artwork = thumbnail ?: "",
        duration = duration,
        channelId = uploaderId ?: "",
        channelName = uploader ?: "",
        index = playlistIndex ?: 0
    )
}

fun PlaylistInfo.toPlaylistMediaData(): PlaylistMediaData {

    return PlaylistMediaData(
        link = webpageUrl ?: "",
        title = title ?: "",
        mediaList = entries?.map { videoInfo ->

            videoInfo.toMediaData()
        }?.toImmutableList() ?: persistentListOf(),
        description = description ?: "",
        channelId = uploaderId ?: "",
        channelName = uploader ?: "",
        extractor = extractor ?: "",
        extractorKey = extractorKey ?: ""
    )
}