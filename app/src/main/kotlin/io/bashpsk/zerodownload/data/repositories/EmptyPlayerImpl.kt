package io.bashpsk.zerodownload.data.repositories

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.zerodownload.domain.repositories.EmptyDatastore
import io.bashpsk.zerodownload.domain.repositories.EmptyPlayer
import javax.inject.Inject

@OptIn(UnstableApi::class)
class EmptyPlayerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val emptyDatastore: EmptyDatastore
) : EmptyPlayer {

    override fun videoPlayer(): ExoPlayer {

        val defaultRenderersFactory = DefaultRenderersFactory(context)
            .setEnableDecoderFallback(true)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)

        val trackSelector = DefaultTrackSelector(context).apply {

            val trackSelectorParameters = buildUponParameters()
                .setAllowVideoMixedMimeTypeAdaptiveness(true)
                .setPreferredAudioLanguage("ta")
                .setIgnoredTextSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                .setRendererDisabled(C.TRACK_TYPE_TEXT, true)

            setParameters(trackSelectorParameters)
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        return ExoPlayer.Builder(context).apply {

            setRenderersFactory(defaultRenderersFactory)
            setTrackSelector(trackSelector)
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        }.build().apply {

            C.USAGE_MEDIA
            playWhenReady = false
            repeatMode = Player.REPEAT_MODE_OFF
            setPlaybackSpeed(1.0F)
            setSeekParameters(SeekParameters.CLOSEST_SYNC)
        }
    }

    override fun audioPlayer(): ExoPlayer {

        return ExoPlayer.Builder(context).apply {

            setTrackSelector(DefaultTrackSelector(context))
            setAudioAttributes(AudioAttributes.DEFAULT, true)
            setHandleAudioBecomingNoisy(true)
            setSeekBackIncrementMs(5 * 1000L)
            setSeekForwardIncrementMs(5 * 1000L)
        }.build().apply {

            C.USAGE_MEDIA
            playWhenReady = false
            repeatMode = Player.REPEAT_MODE_OFF
            setPlaybackSpeed(1.0F)
            setSeekParameters(SeekParameters.CLOSEST_SYNC)
        }
    }
}