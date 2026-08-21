package io.bashpsk.zerodownload.feature.home.event

import androidx.compose.runtime.Stable
import io.bashpsk.emptylibs.formatter.resolution.ResolutionType
import io.bashpsk.zerodownload.core.model.media.AudioQualityType
import io.bashpsk.zerodownload.core.model.media.MediaData
import io.bashpsk.zerodownload.core.model.media.MediaExtensionType
import io.bashpsk.zerodownload.core.model.media.MediaFormatData
import io.bashpsk.zerodownload.core.model.media.MediaFormatType
import io.bashpsk.zerodownload.core.model.media.PlaylistMediaData

@Stable
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