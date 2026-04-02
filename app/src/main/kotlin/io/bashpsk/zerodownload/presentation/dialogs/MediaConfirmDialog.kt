package io.bashpsk.zerodownload.presentation.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.media.AudioQualityType
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.MediaExtensionType
import io.bashpsk.zerodownload.domain.media.MediaFormatData
import io.bashpsk.zerodownload.domain.media.MediaFormatType
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import io.bashpsk.zerodownload.domain.states.MediaSearchState
import io.bashpsk.zerodownload.presentation.media.MediaFormatView
import io.bashpsk.zerodownload.presentation.widgets.DialogTitleView
import io.bashpsk.emptylibs.formatter.resolution.ResolutionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
inline fun MediaConfirmDialog(
    dialogVisibleState: MutableTransitionState<Boolean>,
    searchState: MediaSearchState,
    audioFormat: MediaFormatData?,
    videoFormat: MediaFormatData?,
    selectedPlaylistMedias: ImmutableList<MediaData>,
    crossinline onStartPlayMedia: (
        media: MediaData,
        audio: MediaFormatData?,
        video: MediaFormatData?
    ) -> Unit,
    crossinline onDownloadMedia: (
        media: MediaData,
        audio: MediaFormatData?,
        video: MediaFormatData?,
        videoExt: MediaExtensionType.Video,
        audioExt: MediaExtensionType.Audio
    ) -> Unit,
    crossinline onDownloadPlaylist: (
        playlist: PlaylistMediaData,
        format: MediaFormatType,
        videoQuality: ResolutionType?,
        audioQuality: AudioQualityType,
        videoExt: MediaExtensionType.Video,
        audioExt: MediaExtensionType.Audio
    ) -> Unit
) {

    var videoQuality by rememberSaveable { mutableStateOf<ResolutionType?>(null) }
    var audioQuality by rememberSaveable { mutableStateOf(AudioQualityType.Best) }
    var selectedFormat by rememberSaveable { mutableStateOf(MediaFormatType.VideoAndAudio) }
    var videoExtension by rememberSaveable { mutableStateOf(MediaExtensionType.Video.MP4) }
    var audioExtension by rememberSaveable { mutableStateOf(MediaExtensionType.Audio.MP3) }

    val isVideoFormat by remember(videoFormat) { derivedStateOf { videoFormat != null } }

    val isAudioFormat by remember(audioFormat, isVideoFormat) {
        derivedStateOf { audioFormat != null && !isVideoFormat }
    }

    AnimatedVisibility(
        visibleState = dialogVisibleState,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {

        AlertDialog(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            shape = MaterialTheme.shapes.extraSmall,
            title = {

                DialogTitleView(
                    title = stringResource(R.string.media_confirm_dialog_title),
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                )
            },
            text = {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {

                    when (searchState) {

                        is MediaSearchState.Success -> {

                            if (isVideoFormat) MediaConfirmVideoFormatSelection(
                                modifier = Modifier.fillMaxWidth(),
                                videoExtension = videoExtension,
                                onVideoExtension = { extension ->

                                    videoExtension = extension
                                }
                            )

                            if (isAudioFormat) MediaConfirmAudioFormatSelection(
                                modifier = Modifier.fillMaxWidth(),
                                audioExtension = audioExtension,
                                onAudioExtension = { extension ->

                                    audioExtension = extension
                                }
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                            ) {

                                persistentListOf(videoFormat, audioFormat).forEach { mediaFormat ->

                                    MediaConfirmFormatPreview(mediaFormat = mediaFormat)

                                    Spacer(modifier = Modifier.height(height = 4.dp))
                                }
                            }

                            HorizontalDivider()

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                FilledTonalButton(
                                    onClick = {

                                        onStartPlayMedia(
                                            searchState.media,
                                            audioFormat,
                                            videoFormat
                                        )

                                        dialogVisibleState.targetState = false
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = stringResource(R.string.play)
                                    )

                                    Spacer(modifier = Modifier.width(width = 4.dp))

                                    Text(
                                        text = stringResource(R.string.play),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                ElevatedButton(
                                    onClick = {

                                        onDownloadMedia(
                                            searchState.media,
                                            audioFormat,
                                            videoFormat,
                                            videoExtension,
                                            audioExtension
                                        )

                                        dialogVisibleState.targetState = false
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Filled.Download,
                                        contentDescription = stringResource(R.string.download)
                                    )

                                    Spacer(modifier = Modifier.width(width = 4.dp))

                                    Text(
                                        text = stringResource(R.string.download),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        is MediaSearchState.SuccessPlaylist -> {

                            val newPlaylistData by remember(searchState, selectedPlaylistMedias) {
                                derivedStateOf {
                                    searchState.playlist.copy(mediaList = selectedPlaylistMedias)
                                }
                            }

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${selectedPlaylistMedias.size}/${
                                    searchState.playlist.mediaList.size
                                } ${stringResource(R.string.items_selected)}",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                                itemVerticalAlignment = Alignment.CenterVertically
                            ) {

                                MediaFormatType.entries.forEach { mediaFormat ->

                                    val isSelected by remember(mediaFormat, selectedFormat) {
                                        derivedStateOf { mediaFormat == selectedFormat }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(shape = MaterialTheme.shapes.extraSmall)
                                            .selectable(
                                                role = Role.RadioButton,
                                                selected = isSelected,
                                                onClick = { selectedFormat = mediaFormat }
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        RadioButton(selected = isSelected, onClick = null)

                                        Text(
                                            text = stringResource(mediaFormat.label),
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.labelMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            when (selectedFormat) {

                                MediaFormatType.VideoAndAudio -> {

                                    MediaConfirmVideoQualitySelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        videoQuality = videoQuality,
                                        onVideoQuality = { quality ->

                                            videoQuality = quality
                                        }
                                    )

                                    MediaConfirmAudioQualitySelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        audioQuality = audioQuality,
                                        onAudioQuality = { quality ->

                                            audioQuality = quality
                                        }
                                    )

                                    MediaConfirmVideoFormatSelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        videoExtension = videoExtension,
                                        onVideoExtension = { extension ->

                                            videoExtension = extension
                                        }
                                    )
                                }

                                MediaFormatType.VideoOnly -> {

                                    MediaConfirmVideoQualitySelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        videoQuality = videoQuality,
                                        onVideoQuality = { quality ->

                                            videoQuality = quality
                                        }
                                    )

                                    MediaConfirmVideoFormatSelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        videoExtension = videoExtension,
                                        onVideoExtension = { extension ->

                                            videoExtension = extension
                                        }
                                    )
                                }

                                MediaFormatType.AudioOnly -> {

                                    MediaConfirmAudioQualitySelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        audioQuality = audioQuality,
                                        onAudioQuality = { quality ->

                                            audioQuality = quality
                                        }
                                    )

                                    MediaConfirmAudioFormatSelection(
                                        modifier = Modifier.fillMaxWidth(),
                                        audioExtension = audioExtension,
                                        onAudioExtension = { extension ->

                                            audioExtension = extension
                                        }
                                    )
                                }
                            }

                            HorizontalDivider()

                            ElevatedButton(
                                onClick = {

                                    onDownloadPlaylist(
                                        newPlaylistData,
                                        selectedFormat,
                                        videoQuality,
                                        audioQuality,
                                        videoExtension,
                                        audioExtension
                                    )

                                    dialogVisibleState.targetState = false
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = stringResource(R.string.download)
                                )

                                Spacer(modifier = Modifier.width(width = 4.dp))

                                Text(
                                    text = stringResource(R.string.download),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        else -> Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.media_download_option_disabled),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cancel)
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))

                    Text(
                        text = stringResource(R.string.cancel),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun MediaConfirmVideoQualitySelection(
    modifier: Modifier = Modifier,
    videoQuality: ResolutionType?,
    crossinline onVideoQuality: (quality: ResolutionType?) -> Unit
) {

    var isQualityMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val resolutionList = remember {
        persistentListOf(
            null,
            ResolutionType._144p,
            ResolutionType._240p,
            ResolutionType._360p,
            ResolutionType._480p,
            ResolutionType.HD,
            ResolutionType.FHD,
            ResolutionType.QHD,
            ResolutionType._4K_UHD
        )
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isQualityMenuExpanded,
        onExpandedChange = { isExpanded -> isQualityMenuExpanded = isExpanded }
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = videoQuality?.let { quality ->

                "${quality.label} (${quality.width}x${quality.height})"
            } ?: stringResource(R.string.best),
            onValueChange = {},
            readOnly = true,
            label = {

                Text(
                    text = stringResource(R.string.video_quality_label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            placeholder = {

                Text(
                    text = "${stringResource(R.string.select)} ${
                        stringResource(R.string.video_quality_label)
                    }",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {

                Icon(
                    imageVector = Icons.Filled.OndemandVideo,
                    contentDescription = stringResource(R.string.video_quality_label)
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isQualityMenuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        DropdownMenu(
            expanded = isQualityMenuExpanded,
            onDismissRequest = { isQualityMenuExpanded = false }
        ) {

            resolutionList.forEach { qualityItem ->

                val isSelected by remember(qualityItem, videoQuality) {
                    derivedStateOf { qualityItem == videoQuality }
                }

                DropdownMenuItem(
                    text = {

                        Text(
                            text = qualityItem?.let { quality ->

                                "${quality.label} (${quality.width}x${quality.height})"
                            } ?: stringResource(R.string.best),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingIcon = {

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.select)
                            )
                        }
                    },
                    onClick = {

                        onVideoQuality(qualityItem)
                        isQualityMenuExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun MediaConfirmAudioQualitySelection(
    modifier: Modifier = Modifier,
    audioQuality: AudioQualityType,
    crossinline onAudioQuality: (quality: AudioQualityType) -> Unit
) {

    var isQualityMenuExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isQualityMenuExpanded,
        onExpandedChange = { isExpanded -> isQualityMenuExpanded = isExpanded }
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = stringResource(audioQuality.label),
            onValueChange = {},
            readOnly = true,
            label = {

                Text(
                    text = stringResource(R.string.audio_quality_label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            placeholder = {

                Text(
                    text = "${stringResource(R.string.select)} ${
                        stringResource(R.string.audio_quality_label)
                    }",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {

                Icon(
                    imageVector = Icons.Filled.MusicVideo,
                    contentDescription = stringResource(R.string.audio_quality_label)
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isQualityMenuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        DropdownMenu(
            expanded = isQualityMenuExpanded,
            onDismissRequest = { isQualityMenuExpanded = false }
        ) {

            AudioQualityType.entries.forEach { qualityItem ->

                val isSelected by remember(qualityItem, audioQuality) {
                    derivedStateOf { qualityItem == audioQuality }
                }

                DropdownMenuItem(
                    text = {

                        Text(
                            text = stringResource(qualityItem.label),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingIcon = {

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.select)
                            )
                        }
                    },
                    onClick = {

                        onAudioQuality(qualityItem)
                        isQualityMenuExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun MediaConfirmVideoFormatSelection(
    modifier: Modifier = Modifier,
    videoExtension: MediaExtensionType.Video,
    crossinline onVideoExtension: (extension: MediaExtensionType.Video) -> Unit
) {

    var isFormatMenuExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isFormatMenuExpanded,
        onExpandedChange = { isExpanded -> isFormatMenuExpanded = isExpanded }
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = videoExtension.toLabel(),
            onValueChange = {},
            readOnly = true,
            label = {

                Text(
                    text = stringResource(R.string.video_format_label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            placeholder = {

                Text(
                    text = "${stringResource(R.string.select)} ${
                        stringResource(R.string.video_format_label)
                    }",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {

                Icon(
                    imageVector = Icons.Filled.VideoFile,
                    contentDescription = stringResource(R.string.video_format_label)
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFormatMenuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        DropdownMenu(
            expanded = isFormatMenuExpanded,
            onDismissRequest = { isFormatMenuExpanded = false }
        ) {

            MediaExtensionType.Video.entries.forEach { extensionItem ->

                val isSelected by remember(extensionItem, videoExtension) {
                    derivedStateOf { extensionItem == videoExtension }
                }

                DropdownMenuItem(
                    text = {

                        Text(
                            text = extensionItem.toLabel(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingIcon = {

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.select)
                            )
                        }
                    },
                    onClick = {

                        onVideoExtension(extensionItem)
                        isFormatMenuExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun MediaConfirmAudioFormatSelection(
    modifier: Modifier = Modifier,
    audioExtension: MediaExtensionType.Audio,
    crossinline onAudioExtension: (extension: MediaExtensionType.Audio) -> Unit
) {

    var isFormatMenuExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isFormatMenuExpanded,
        onExpandedChange = { isExpanded -> isFormatMenuExpanded = isExpanded }
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = audioExtension.toLabel(),
            onValueChange = {},
            readOnly = true,
            label = {

                Text(
                    text = stringResource(R.string.audio_format_label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            placeholder = {

                Text(
                    text = "${stringResource(R.string.select)} ${
                        stringResource(R.string.audio_format_label)
                    }",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {

                Icon(
                    imageVector = Icons.Filled.AudioFile,
                    contentDescription = stringResource(R.string.audio_format_label)
                )
            },
            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFormatMenuExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        DropdownMenu(
            expanded = isFormatMenuExpanded,
            onDismissRequest = { isFormatMenuExpanded = false }
        ) {

            MediaExtensionType.Audio.entries.forEach { extensionItem ->

                val isSelected by remember(extensionItem, audioExtension) {
                    derivedStateOf { extensionItem == audioExtension }
                }

                DropdownMenuItem(
                    text = {

                        Text(
                            text = extensionItem.toLabel(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingIcon = {

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.select)
                            )
                        }
                    },
                    onClick = {

                        onAudioExtension(extensionItem)
                        isFormatMenuExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MediaConfirmFormatPreview(mediaFormat: MediaFormatData?) {

    mediaFormat?.let { media ->

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                when (media) {

                    is MediaFormatData.VideoAndAudio -> R.string.video_audio_media_category_title
                    is MediaFormatData.VideoOnly -> R.string.video_only_media_category_title
                    is MediaFormatData.AudioOnly -> R.string.audio_only_media_category_title
                    is MediaFormatData.Unknown -> R.string.unknown_media_category_title
                }
            ),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        MediaFormatView(
            modifier = Modifier.fillMaxWidth(),
            mediaFormat = media,
            isMediaSelect = false,
            isSelected = false,
            onMediaClick = {},
            onMediaLongClick = {}
        )
    }
}