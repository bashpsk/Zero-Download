package io.bashpsk.zerodownload.core.domain.repositories

import androidx.media3.exoplayer.ExoPlayer

interface EmptyPlayer {

    fun videoPlayer(): ExoPlayer

    fun audioPlayer(): ExoPlayer
}