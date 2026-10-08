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
import io.bashpsk.zerodownload.feature.home.state.SavedStateKey
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
        key = SavedStateKey.HOME_OPTION_MENU,
        initialValue = false
    )

    val isMediaSelect = savedStateHandle.getStateFlow(
        key = SavedStateKey.HOME_MEDIA_SELECT,
        initialValue = false
    )

    val selectedAudioFormat = savedStateHandle.getStateFlow<MediaFormatData?>(
        key = SavedStateKey.HOME_SELECTED_AUDIO,
        initialValue = null
    )

    val selectedVideoFormat = savedStateHandle.getStateFlow<MediaFormatData?>(
        key = SavedStateKey.HOME_SELECTED_VIDEO,
        initialValue = null
    )

    val isScreenRefreshing = searchMediaState.flatMapLatest { searchState ->

        flowOf(value = searchState == MediaSearchState.Searching)
    }.flowOn(context = Dispatchers.Default).stateInWhileSubscribed(initial = false)

    fun onUIEvent(uiEvent: HomeUIEvent) {

        when (uiEvent) {

            is HomeUIEvent.DoNothing -> {}

            is HomeUIEvent.MediaDownloadCombined -> {

                viewModelScope.launch(context = Dispatchers.IO) {

                    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                    val rootDirectory = File(downloadsDirectory, ConstantString.ROOT_FOLDER)

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
            }

            is HomeUIEvent.MediaDownloadPlaylist -> {

                viewModelScope.launch(context = Dispatchers.IO) {

                    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                    val rootDirectory = File(downloadsDirectory, ConstantString.ROOT_FOLDER)

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
            }

            is HomeUIEvent.MediaSearch -> {

                viewModelScope.launch(context = Dispatchers.IO) {

                    savedStateHandle[SavedStateKey.HOME_MEDIA_SELECT] = false
                    savedStateHandle[SavedStateKey.HOME_SELECTED_AUDIO] = null
                    savedStateHandle[SavedStateKey.HOME_SELECTED_VIDEO] = null

                    emptyMedia.getMediaSearch(
                        link = uiEvent.link
                    ).collectLatest { resultLatest ->

                        searchMediaState.update { resultLatest }
                    }
                }
            }

            is HomeUIEvent.MediaSelect -> {

                savedStateHandle[SavedStateKey.HOME_MEDIA_SELECT] = uiEvent.isVisible
            }

            is HomeUIEvent.OptionMenu -> {

                savedStateHandle[SavedStateKey.HOME_OPTION_MENU] = uiEvent.isVisible
            }

            is HomeUIEvent.ResetSelectedFormat -> {

                savedStateHandle[SavedStateKey.HOME_MEDIA_SELECT] = false
                savedStateHandle[SavedStateKey.HOME_SELECTED_AUDIO] = null
                savedStateHandle[SavedStateKey.HOME_SELECTED_VIDEO] = null
            }

            is HomeUIEvent.SetSelectAudioFormat -> {

                savedStateHandle[
                    SavedStateKey.HOME_SELECTED_AUDIO
                ] = uiEvent.media.takeIf { media ->

                    media.formatId != selectedAudioFormat.value?.formatId
                }
            }

            is HomeUIEvent.SetSelectPlaylistMedia -> {

                selectedPlaylistMedias.update { medias ->

                    medias.find { media ->

                        media.link == uiEvent.media.link
                    }?.let { existMedia ->

                        medias.removing(element = existMedia)
                    } ?: medias.adding(element = uiEvent.media)
                }
            }

            is HomeUIEvent.SetSelectVideoFormat -> {

                savedStateHandle[
                    SavedStateKey.HOME_SELECTED_VIDEO
                ] = uiEvent.media.takeIf { media ->

                    media.formatId != selectedVideoFormat.value?.formatId
                }
            }
        }
    }
}