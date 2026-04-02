package io.bashpsk.zerodownload.domain.repositories

import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import io.bashpsk.zerodownload.domain.states.MediaSearchState
import kotlinx.coroutines.flow.Flow

interface EmptyMedia {

    fun getMediaSearch(link: String): Flow<MediaSearchState>

    suspend fun getMediaData(link: String): MediaData?

    suspend fun getPlaylistMediaData(link: String): PlaylistMediaData?

    suspend fun setScanMediaPath(path: String): String

    suspend fun setYtDlDestroy(id: String): Boolean
}