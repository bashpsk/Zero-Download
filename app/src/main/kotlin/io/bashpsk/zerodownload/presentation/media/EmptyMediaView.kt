package io.bashpsk.zerodownload.presentation.media

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.media.EmptyMediaType

@Composable
fun EmptyMediaView(
    modifier: Modifier = Modifier,
    isEmptyMediaView: Boolean,
    emptyMediaType: EmptyMediaType
) {

    val infiniteTransition = rememberInfiniteTransition(stringResource(R.string.no_media_found))

    val mediaAlphaLevel = when (isEmptyMediaView) {

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

        EmptyMediaType.EXTRA_SMALL -> MaterialTheme.colorScheme.onPrimaryContainer
        EmptyMediaType.SMALL -> MaterialTheme.colorScheme.onSecondaryContainer
        EmptyMediaType.MEDIUM -> MaterialTheme.colorScheme.onTertiaryContainer
        EmptyMediaType.LARGE -> MaterialTheme.colorScheme.onErrorContainer
    }

    val mediaLabelStyle = when (emptyMediaType) {

        EmptyMediaType.EXTRA_SMALL -> MaterialTheme.typography.labelMedium
        EmptyMediaType.SMALL -> MaterialTheme.typography.titleSmall
        EmptyMediaType.MEDIUM -> MaterialTheme.typography.titleMedium
        EmptyMediaType.LARGE -> MaterialTheme.typography.headlineSmall
    }

    val mediaIconSize by remember(emptyMediaType) {
        derivedStateOf {
            when (emptyMediaType) {

                EmptyMediaType.EXTRA_SMALL -> 50.dp
                EmptyMediaType.SMALL -> 60.dp
                EmptyMediaType.MEDIUM -> 80.dp
                EmptyMediaType.LARGE -> 100.dp
            }
        }
    }

    AnimatedVisibility(
        visible = isEmptyMediaView,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
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