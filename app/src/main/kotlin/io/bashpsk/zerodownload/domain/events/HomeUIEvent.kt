package io.bashpsk.zerodownload.domain.events

import io.bashpsk.zerodownload.domain.media.AudioQualityType
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.MediaExtensionType
import io.bashpsk.zerodownload.domain.media.MediaFormatData
import io.bashpsk.zerodownload.domain.media.MediaFormatType
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import io.bashpsk.emptylibs.formatter.resolution.ResolutionType

sealed interface HomeUIEvent {

    data object DoNothing : HomeUIEvent

    data class MediaDownloadCombined(
        val media: MediaData,
        val audio: MediaFormatData?,
        val video: MediaFormatData?,
        val videoExt: MediaExtensionType.Video?,
        val audioExt: MediaExtensionType.Audio?
    ) : HomeUIEvent

    data class MediaDownloadPlaylist(
        val playlist: PlaylistMediaData,
        val format: MediaFormatType,
        val videoQuality: ResolutionType?,
        val audioQuality: AudioQualityType,
        val videoExt: MediaExtensionType.Video?,
        val audioExt: MediaExtensionType.Audio?
    ) : HomeUIEvent

    data class MediaSearch(val link: String) : HomeUIEvent

    data class MediaSelect(val isVisible: Boolean) : HomeUIEvent

    data class OptionMenu(val isVisible: Boolean) : HomeUIEvent

    data object ResetSelectedFormat : HomeUIEvent

    data class SetSelectAudioFormat(val media: MediaFormatData) : HomeUIEvent

    data class SetSelectPlaylistMedia(val media: MediaData) : HomeUIEvent

    data class SetSelectVideoFormat(val media: MediaFormatData) : HomeUIEvent

    data class StartMediaPlayer(
        val media: MediaData,
        val audio: MediaFormatData?,
        val video: MediaFormatData?
    ) : HomeUIEvent
}