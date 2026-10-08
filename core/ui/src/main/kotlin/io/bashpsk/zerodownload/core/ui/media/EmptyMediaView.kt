package io.bashpsk.zerodownload.core.ui.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.core.model.media.EmptyMediaType
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun EmptyMediaView(
    modifier: Modifier = Modifier,
    isEmptyMediaView: Boolean?,
    emptyMediaType: EmptyMediaType
) {

    isEmptyMediaView?.let { isEmptyMediaViewVisible ->

        val infiniteTransition = rememberInfiniteTransition(
            label = stringResource(R.string.no_media_found)
        )

        var isAnimationVisible by rememberSaveable { mutableStateOf(false) }

        val mediaAlphaLevel = when (isAnimationVisible) {

            true -> infiniteTransition.animateFloat(
                initialValue = 0.80F,
                targetValue = 0.003F,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3000, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = stringResource(R.string.no_media_found)
            ).value

            false -> 1.0F
        }

        val mediaContentColor = when (emptyMediaType) {

            EmptyMediaType.ExtraSmall -> MaterialTheme.colorScheme.onPrimaryContainer
            EmptyMediaType.Small -> MaterialTheme.colorScheme.onSecondaryContainer
            EmptyMediaType.Medium -> MaterialTheme.colorScheme.onTertiaryContainer
            EmptyMediaType.Large -> MaterialTheme.colorScheme.onErrorContainer
        }

        val mediaLabelStyle = when (emptyMediaType) {

            EmptyMediaType.ExtraSmall -> MaterialTheme.typography.labelMedium
            EmptyMediaType.Small -> MaterialTheme.typography.titleSmall
            EmptyMediaType.Medium -> MaterialTheme.typography.titleMedium
            EmptyMediaType.Large -> MaterialTheme.typography.headlineSmall
        }

        val mediaIconSize by remember(emptyMediaType) {
            derivedStateOf {
                when (emptyMediaType) {

                    EmptyMediaType.ExtraSmall -> 50.dp
                    EmptyMediaType.Small -> 60.dp
                    EmptyMediaType.Medium -> 80.dp
                    EmptyMediaType.Large -> 100.dp
                }
            }
        }

        AnimatedVisibility(
            visible = isEmptyMediaViewVisible,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .onVisibilityChanged { isVisible ->

                        isAnimationVisible = isVisible
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {

                Icon(
                    modifier = Modifier.size(size = mediaIconSize),
                    imageVector = Icons.TwoTone.FolderOpen,
                    tint = mediaContentColor.copy(alpha = mediaAlphaLevel),
                    contentDescription = stringResource(R.string.no_media_found)
                )

                Text(
                    text = stringResource(R.string.no_media_found),
                    textAlign = TextAlign.Center,
                    style = mediaLabelStyle,
                    color = mediaContentColor.copy(alpha = mediaAlphaLevel),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}