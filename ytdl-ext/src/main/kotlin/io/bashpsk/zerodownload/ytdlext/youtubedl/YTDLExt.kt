package io.bashpsk.zerodownload.ytdlext.youtubedl

import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDL.CanceledException
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo
import io.bashpsk.zerodownload.ytdlext.mapper.PlaylistInfo
import kotlinx.io.IOException

@Throws(YoutubeDLException::class, InterruptedException::class, CanceledException::class)
fun YoutubeDL.getPlaylistInfo(
    request: YoutubeDLRequest,
    processId: String
): PlaylistInfo {

    request.apply {

        addOption(option = "--dump-single-json")
    }

    val response = execute(request = request, processId = processId, callback = null)

    return try {

        objectMapper.readValue(response.out, PlaylistInfo::class.java)
    } catch (exception: IOException) {

        throw YoutubeDLException(message = "Unable To Parse Playlist Information", e = exception)
    }
}

@Throws(YoutubeDLException::class, InterruptedException::class, CanceledException::class)
fun YoutubeDL.getInfo(url: String, processId: String): VideoInfo {

    return getInfo(request = YoutubeDLRequest(url = url), processId = processId)
}

@Throws(YoutubeDLException::class, InterruptedException::class, CanceledException::class)
fun YoutubeDL.getInfo(request: YoutubeDLRequest, processId: String): VideoInfo {

    request.apply {

        addOption("--dump-json")
    }

    val response = execute(request = request, processId = processId, callback = null)

    return try {

        objectMapper.readValue(response.out, VideoInfo::class.java)
    } catch (exception: IOException) {

        throw YoutubeDLException(message = "Unable to parse video information", e = exception)
    }
}