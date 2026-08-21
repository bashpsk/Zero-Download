package io.bashpsk.zerodownload.core.domain.repositories

import io.bashpsk.zerodownload.core.domain.states.MediaSearchState
import io.bashpsk.zerodownload.core.model.media.MediaData
import io.bashpsk.zerodownload.core.model.media.PlaylistMediaData
import kotlinx.coroutines.flow.Flow

interface EmptyMedia {

    fun getMediaSearch(link: String): Flow<MediaSearchState>

    suspend fun getMediaData(link: String): MediaData?

    suspend fun getPlaylistMediaData(link: String): PlaylistMediaData?

    suspend fun setScanMediaPath(path: String): String

    suspend fun setYtDlDestroy(id: String): Boolean
}