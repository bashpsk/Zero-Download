package io.bashpsk.zerodownload.presentation.appui.home

import androidx.annotation.StringRes
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.domain.media.MediaFormatData
import io.bashpsk.zerodownload.domain.states.MediaSearchState
import io.bashpsk.zerodownload.presentation.media.MediaFormatView
import io.bashpsk.zerodownload.presentation.media.MediaView
import io.bashpsk.zerodownload.presentation.media.PlaylistMediaView
import io.bashpsk.zerodownload.presentation.playlist.PlaylistView
import kotlinx.collections.immutable.ImmutableList

inline fun LazyGridScope.MediaSearchStateView(
    searchState: MediaSearchState,
    mediaDetailsDialogVisibleState: MutableTransitionState<Boolean>,
    selectedAudioFormat: MediaFormatData?,
    selectedVideoFormat: MediaFormatData?,
    isMediaSelect: Boolean,
    selectedPlaylistMedias: ImmutableList<MediaData>,
    crossinline onVideoClick: (format: MediaFormatData) -> Unit,
    crossinline onVideoLongClick: (format: MediaFormatData) -> Unit,
    crossinline onAudioClick: (format: MediaFormatData) -> Unit,
    crossinline onAudioLongClick: (format: MediaFormatData) -> Unit,
    crossinline onSelectPlaylistMedia: (media: MediaData) -> Unit,
) {

    when (searchState) {

        is MediaSearchState.Init -> mediaSearchStateMessageText(R.string.link_input_instruction)

        is MediaSearchState.Searching -> mediaSearchStateMessageText(
            R.string.searching_wait_instruction
        )

        is MediaSearchState.Failed -> mediaSearchStateMessageText(R.string.searching_failed)

        is MediaSearchState.Success -> {

            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                MediaView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    mediaData = searchState.media,
                    isMediaSelect = false,
                    isSelected = false,
                    onMediaClick = { _ ->

                        mediaDetailsDialogVisibleState.targetState = true
                    }
                )
            }

            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                MediaCategoryTitle(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    title = stringResource(R.string.video_audio_media_category_title),
                    isVisible = searchState.media.mediaFormats.isNotEmpty()
                )
            }

            items(
                items = searchState.media.mediaFormats,
                key = { mediaFormatData -> mediaFormatData.uuid }
            ) { mediaFormatData ->

                val isSelected by remember(mediaFormatData, selectedVideoFormat) {
                    derivedStateOf { mediaFormatData.formatId == selectedVideoFormat?.formatId }
                }

                MediaFormatView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    mediaFormat = mediaFormatData,
                    isMediaSelect = isMediaSelect,
                    isSelected = isSelected,
                    onMediaClick = onVideoClick,
                    onMediaLongClick = onVideoLongClick
                )
            }

            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                MediaCategoryTitle(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    title = stringResource(R.string.audio_only_media_category_title),
                    isVisible = searchState.media.audioFormats.isNotEmpty()
                )
            }

            items(
                items = searchState.media.audioFormats,
                key = { mediaFormatData -> mediaFormatData.uuid }
            ) { mediaFormatData ->

                val isSelected by remember(mediaFormatData, selectedAudioFormat) {
                    derivedStateOf { mediaFormatData.formatId == selectedAudioFormat?.formatId }
                }

                MediaFormatView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    mediaFormat = mediaFormatData,
                    isMediaSelect = isMediaSelect,
                    isSelected = isSelected,
                    onMediaClick = onAudioClick,
                    onMediaLongClick = onAudioLongClick
                )
            }

            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                MediaCategoryTitle(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    title = stringResource(R.string.video_only_media_category_title),
                    isVisible = searchState.media.videoFormats.isNotEmpty()
                )
            }

            items(
                items = searchState.media.videoFormats,
                key = { mediaFormatData -> mediaFormatData.uuid }
            ) { mediaFormatData ->

                val isSelected by remember(mediaFormatData, selectedVideoFormat) {
                    derivedStateOf { mediaFormatData.formatId == selectedVideoFormat?.formatId }
                }

                MediaFormatView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    mediaFormat = mediaFormatData,
                    isMediaSelect = isMediaSelect,
                    isSelected = isSelected,
                    onMediaClick = onVideoClick,
                    onMediaLongClick = onVideoLongClick
                )
            }
        }

        is MediaSearchState.SuccessPlaylist -> {

            item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

                PlaylistView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    playlistData = searchState.playlist
                )
            }

            items(
                items = searchState.playlist.mediaList,
                key = { mediaData -> mediaData.uuid }
            ) { mediaData ->

                val isSelected by remember(mediaData, selectedPlaylistMedias) {
                    derivedStateOf {
                        selectedPlaylistMedias.any { media -> media.link == mediaData.link }
                    }
                }

                PlaylistMediaView(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    mediaData = mediaData,
                    isMediaSelect = true,
                    isSelected = isSelected,
                    onMediaClick = onSelectPlaylistMedia
                )
            }
        }
    }
}

fun LazyGridScope.mediaSearchStateMessageText(@StringRes id: Int) {

    item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

        Text(
            modifier = Modifier.animateItem(
                fadeInSpec = tween(durationMillis = 250),
                fadeOutSpec = tween(durationMillis = 100),
                placementSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            ),
            text = stringResource(id),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}