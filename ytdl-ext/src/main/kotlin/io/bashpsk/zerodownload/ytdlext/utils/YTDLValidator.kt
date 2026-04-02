package io.bashpsk.zerodownload.ytdlext.utils

fun String.hasPlaylistLink(): Boolean {

    return startsWith(prefix = "https://www.youtube.com/playlist") ||
            startsWith(prefix = "https://youtube.com/playlist") ||
            startsWith(prefix = "https://youtu.be/playlist")
}