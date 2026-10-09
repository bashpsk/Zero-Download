package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import io.bashpsk.zerodownload.core.model.controls.PlayerAction
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    playbackState: Int = 0,
    isPlayerPlaying: Boolean = false,
    onClick: (action: PlayerAction) -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.40F),
    contentColor: Color = LocalContentColor.current
) {

    val isPlayPauseButtonVisible by remember(playbackState) {
        derivedStateOf { playbackState != Player.STATE_BUFFERING }
    }

    val playerActionIcon by remember(playbackState, isPlayerPlaying) {
        derivedStateOf {
            when (playbackState) {

                Player.STATE_IDLE if !isPlayerPlaying -> Icons.Filled.PlayArrow
                Player.STATE_BUFFERING if !isPlayerPlaying -> Icons.Filled.PlayArrow
                Player.STATE_READY if !isPlayerPlaying -> Icons.Filled.PlayArrow
                Player.STATE_READY if isPlayerPlaying -> Icons.Filled.Pause
                Player.STATE_ENDED -> Icons.Filled.Replay
                else -> Icons.Filled.PlayArrow
            }
        }
    }

    val onPlayerActionClick = remember(playbackState, isPlayerPlaying) {
        {
            val playerAction = when (playbackState) {

                Player.STATE_IDLE if !isPlayerPlaying -> PlayerAction.Prepare
                Player.STATE_BUFFERING if !isPlayerPlaying -> PlayerAction.Buffer
                Player.STATE_READY if !isPlayerPlaying -> PlayerAction.Play
                Player.STATE_READY if isPlayerPlaying -> PlayerAction.Pause
                Player.STATE_ENDED -> PlayerAction.Restart
                else -> PlayerAction.Prepare
            }

            onClick(playerAction)
        }
    }

    AnimatedVisibility(
        visible = isPlayPauseButtonVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {

        ButtonTooltipBox(tooltip = stringResource(R.string.play_pause_media)) {

            IconButton(
                modifier = modifier.size(
                    IconButtonDefaults.largeContainerSize(
                        IconButtonDefaults.IconButtonWidthOption.Uniform
                    )
                ),
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                onClick = onPlayerActionClick
            ) {

                Icon(
                    modifier = Modifier.size(IconButtonDefaults.largeIconSize),
                    imageVector = playerActionIcon,
                    contentDescription = stringResource(R.string.play_pause_media)
                )
            }
        }
    }
}