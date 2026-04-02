package io.bashpsk.zerodownload.domain.states

import android.os.Parcelable
import androidx.compose.runtime.Stable
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Stable
@Serializable
@Parcelize
sealed interface MediaSearchState : Parcelable {

    data object Init : MediaSearchState

    data object Searching : MediaSearchState

    data class Success(val media: MediaData) : MediaSearchState

    data class SuccessPlaylist(val playlist: PlaylistMediaData) : MediaSearchState

    data class Failed(val message: String) : MediaSearchState
}