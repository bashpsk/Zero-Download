package io.bashpsk.zerodownload.feature.home.screen

import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bashpsk.zerodownload.core.common.log.setDebug
import io.bashpsk.zerodownload.core.common.viewmodel.stateInWhileSubscribed
import io.bashpsk.zerodownload.core.domain.repositories.EmptyMedia
import io.bashpsk.zerodownload.core.domain.repositories.EmptyWorker
import io.bashpsk.zerodownload.core.domain.states.MediaSearchState
import io.bashpsk.zerodownload.core.model.media.AudioQualityType
import io.bashpsk.zerodownload.core.model.media.MediaData
import io.bashpsk.zerodownload.core.model.media.MediaFormatData
import io.bashpsk.zerodownload.core.model.media.MediaFormatType
import io.bashpsk.zerodownload.core.model.resources.ConstantCommand
import io.bashpsk.zerodownload.core.model.resources.ConstantString
import io.bashpsk.zerodownload.feature.home.event.HomeUIEvent
import io.bashpsk.zerodownload.feature.home.state.KeyMediaSelect
import io.bashpsk.zerodownload.feature.home.state.KeyOptionMenu
import io.bashpsk.zerodownload.feature.home.state.KeySelectedAudio
import io.bashpsk.zerodownload.feature.home.state.KeySelectedVideo
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val emptyMedia: EmptyMedia,
    private val emptyWorker: EmptyWorker
) : ViewModel() {

    val searchMediaState: StateFlow<MediaSearchState>
        field = MutableStateFlow<MediaSearchState>(MediaSearchState.Init)

    val selectedPlaylistMedias: StateFlow<PersistentList<MediaData>>
        field = MutableStateFlow(persistentListOf())

    val isOptionMenu = savedStateHandle.getStateFlow(
        key = KeyOptionMenu,
        initialValue = false
    )

    val isMediaSelect = savedStateHandle.getStateFlow(
        key = KeyMediaSelect,
        initialValue = false
    )

    val selectedAudioFormat = savedStateHandle.getStateFlow<MediaFormatData?>(
        key = KeySelectedAudio,
        initialValue = null
    )

    val selectedVideoFormat = savedStateHandle.getStateFlow<MediaFormatData?>(
        key = KeySelectedVideo,
        initialValue = null
    )

    val isScreenRefreshing = searchMediaState.flatMapLatest { searchState ->

        flowOf(value = searchState == MediaSearchState.Searching)
    }.flowOn(context = Dispatchers.Default).stateInWhileSubscribed(initial = false)

    fun onUIEvent(uiEvent: HomeUIEvent) = viewModelScope.launch(context = Dispatchers.Default) {

        when (uiEvent) {

            is HomeUIEvent.DoNothing -> {}

            is HomeUIEvent.MediaDownloadCombined -> {

                val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )

                val rootDirectory = File(downloadsDirectory, ConstantString.APP_NAME)

                val formatIdsRegex = Regex(pattern = "^\\+|\\+$")

                val formatIds = "${
                    uiEvent.video?.formatId ?: ""
                }+${
                    uiEvent.audio?.formatId
                }".replace(regex = formatIdsRegex, replacement = "")

                val fileExtConversion = when {

                    uiEvent.videoExt != null -> "--recode-video ${uiEvent.videoExt.ext}"

                    uiEvent.audioExt != null && uiEvent.video == null -> {
                        "--extract-audio --audio-format ${uiEvent.audioExt.ext}"
                    }

                    else -> ""
                }

                val command = "${
                    uiEvent.media.link
                } --format $formatIds --restrict-filenames $fileExtConversion --output ${
                    rootDirectory.path
                }${File.separatorChar}${ConstantCommand.MEDIA_TITLE_EXT_DEFAULT}"

                emptyWorker.setYtDlCommand(
                    command = command.also { it.setDebug() },
                    title = uiEvent.media.title
                ).collectLatest { workInfoLatest ->

                }
            }

            is HomeUIEvent.MediaDownloadPlaylist -> {

                val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )

                val rootDirectory = File(downloadsDirectory, ConstantString.APP_NAME)

                val videoFormat = "bestvideo${
                    uiEvent.videoQuality?.height?.let { height -> "[height<=$height]" } ?: ""
                }"

                val audioFormat = "bestaudio${
                    uiEvent.audioQuality.takeIf { quality ->

                        quality != AudioQualityType.Best
                    }?.let { quality -> "[abr<=${quality.bitrate}]" } ?: ""
                }"

                val formatSelection = when (uiEvent.format) {

                    MediaFormatType.VideoAndAudio -> "$videoFormat+$audioFormat"
                    MediaFormatType.VideoOnly -> videoFormat
                    MediaFormatType.AudioOnly -> audioFormat
                }

                val command = "${uiEvent.playlist.link} --playlist-items ${
                    uiEvent.playlist.mediaList.joinToString(separator = ",") { media ->

                        "${media.index}"
                    }
                } --format $formatSelection --restrict-filenames --output ${
                    rootDirectory.path
                }${File.separatorChar}${ConstantCommand.MEDIA_TITLE_EXT_DEFAULT}"

                val commandTitle = "Playlist: ${
                    uiEvent.playlist.title
                } (${uiEvent.playlist.mediaList.size} items)"

                emptyWorker.setYtDlCommand(
                    command = command.also { it.setDebug() },
                    title = commandTitle
                ).collectLatest { workInfoLatest ->

                }
            }

            is HomeUIEvent.MediaSearch -> {

                savedStateHandle[KeyMediaSelect] = false
                savedStateHandle[KeySelectedAudio] = null
                savedStateHandle[KeySelectedVideo] = null

                emptyMedia.getMediaSearch(
                    link = uiEvent.link
                ).collectLatest { resultLatest ->

                    searchMediaState.update { resultLatest }
                }
            }

            is HomeUIEvent.MediaSelect -> {

                savedStateHandle[KeyMediaSelect] = uiEvent.isVisible
            }

            is HomeUIEvent.OptionMenu -> {

                savedStateHandle[KeyOptionMenu] = uiEvent.isVisible
            }

            is HomeUIEvent.ResetSelectedFormat -> {

                savedStateHandle[KeyMediaSelect] = false
                savedStateHandle[KeySelectedAudio] = null
                savedStateHandle[KeySelectedVideo] = null
            }

            is HomeUIEvent.SetSelectAudioFormat -> {

                savedStateHandle[KeySelectedAudio] = uiEvent.media.takeIf { media ->

                    media.formatId != selectedAudioFormat.value?.formatId
                }
            }

            is HomeUIEvent.SetSelectPlaylistMedia -> {

                selectedPlaylistMedias.update { medias ->

                    medias.find { media ->

                        media.link == uiEvent.media.link
                    }?.let { existMedia ->

                        medias.remove(element = existMedia)
                    } ?: medias.add(element = uiEvent.media)
                }
            }

            is HomeUIEvent.SetSelectVideoFormat -> {

                savedStateHandle[KeySelectedVideo] = uiEvent.media.takeIf { media ->

                    media.formatId != selectedVideoFormat.value?.formatId
                }
            }

            is HomeUIEvent.StartMediaPlayer -> {

            }
        }
    }
}