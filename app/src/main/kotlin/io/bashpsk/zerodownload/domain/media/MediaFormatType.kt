package io.bashpsk.zerodownload.domain.media

import androidx.annotation.StringRes
import io.bashpsk.zerodownload.R

enum class MediaFormatType(@param:StringRes val label: Int) {

    VideoAndAudio(R.string.video_audio_media_category_title),
    VideoOnly(R.string.video_only_media_category_title),
    AudioOnly(R.string.audio_only_media_category_title);
}