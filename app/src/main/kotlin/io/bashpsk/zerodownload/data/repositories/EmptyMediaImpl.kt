package io.bashpsk.zerodownload.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bashpsk.emptylibs.storage.extension.findMimeType
import io.bashpsk.zerodownload.data.extension.toMediaData
import io.bashpsk.zerodownload.data.extension.toMediaFormatData
import io.bashpsk.zerodownload.data.extension.toPlaylistMediaData
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.MediaFormatData
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import io.bashpsk.zerodownload.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.domain.resources.ConstantKey
import io.bashpsk.zerodownload.domain.states.MediaSearchState
import io.bashpsk.zerodownload.domain.utils.LOG_TAG
import io.bashpsk.zerodownload.ytdlext.utils.hasPlaylistLink
import io.bashpsk.zerodownload.ytdlext.youtubedl.getInfo
import io.bashpsk.zerodownload.ytdlext.youtubedl.getPlaylistInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.coroutines.resume

@SuppressLint("Range")
class EmptyMediaImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EmptyMedia {

    override fun getMediaSearch(link: String): Flow<MediaSearchState> {

        return flow {

            emit(value = MediaSearchState.Searching)

            try {

                when (link.hasPlaylistLink()) {

                    true -> getPlaylistMediaData(link = link)?.let { newData ->

                        emit(value = MediaSearchState.SuccessPlaylist(playlist = newData))
                    } ?: emit(value = MediaSearchState.Failed(message = "Playlist not found."))

                    false -> getMediaData(link = link)?.let { newData ->

                        emit(value = MediaSearchState.Success(media = newData))
                    } ?: emit(value = MediaSearchState.Failed(message = "Media not found."))
                }
            } catch (exception: Exception) {

                currentCoroutineContext().ensureActive()
                emit(value = MediaSearchState.Failed(message = exception.message ?: "Unknown."))
                Log.e(LOG_TAG, exception.message, exception)
            }
        }.flowOn(context = Dispatchers.IO)
    }

    override suspend fun getMediaData(
        link: String
    ): MediaData? = withContext(context = Dispatchers.IO) {

        return@withContext try {

            setYtDlDestroy(id = ConstantKey.YT_DL_SEARCH_ID)

            val dlResponse = YoutubeDL.getInstance().getInfo(
                url = link,
                processId = ConstantKey.YT_DL_SEARCH_ID
            )

            val formatList = dlResponse.formats?.map { videoFormat ->

                currentCoroutineContext().ensureActive()
                videoFormat.toMediaFormatData()
            }?.toImmutableList() ?: persistentListOf()

            val mediaFormats = formatList.filterIsInstance<MediaFormatData.VideoAndAudio>()
                .toImmutableList()

            val audioFormats = formatList.filterIsInstance<MediaFormatData.AudioOnly>()
                .toImmutableList()

            val videoFormats = formatList.filterIsInstance<MediaFormatData.VideoOnly>()
                .toImmutableList()

            dlResponse.toMediaData().copy(
                mediaFormats = mediaFormats,
                audioFormats = audioFormats,
                videoFormats = videoFormats,
            )
        } catch (exception: Exception) {

            currentCoroutineContext().ensureActive()
            Log.e(LOG_TAG, exception.message, exception)
            null
        }
    }

    override suspend fun getPlaylistMediaData(
        link: String
    ): PlaylistMediaData? = withContext(context = Dispatchers.IO) {

        return@withContext try {

            setYtDlDestroy(id = ConstantKey.YT_DL_SEARCH_ID)

            val commandRegex = "\"([^\"]*)\"|(\\S+)"
            val dlRequest = YoutubeDLRequest(urls = Collections.emptyList())
            val commandMatcher = Pattern.compile(commandRegex).matcher(link)

            while (commandMatcher.find()) {

                currentCoroutineContext().ensureActive()

                (commandMatcher.group(1).takeIf { command ->

                    command != null
                } ?: commandMatcher.group(2))?.let { matchedCommand ->

                    dlRequest.addOption(option = matchedCommand)
                }
            }

            val dlResponse = YoutubeDL.getInstance().getPlaylistInfo(
                request = dlRequest,
                processId = ConstantKey.YT_DL_SEARCH_ID
            )

            dlResponse.toPlaylistMediaData()
        } catch (exception: Exception) {

            currentCoroutineContext().ensureActive()
            Log.e(LOG_TAG, exception.message, exception)
            null
        }
    }

    override suspend fun setScanMediaPath(path: String): String {

        return suspendCancellableCoroutine { continuation ->

            MediaScannerConnection.scanFile(
                context,
                arrayOf(path),
                arrayOf(File(path).findMimeType())
            ) { path, _ ->

                continuation.resume(value = path)
            }
        }
    }

    override suspend fun setYtDlDestroy(id: String): Boolean {

        return suspendCancellableCoroutine { continuation ->

            continuation.resume(value = YoutubeDL.getInstance().destroyProcessById(id = id))
        }
    }
}