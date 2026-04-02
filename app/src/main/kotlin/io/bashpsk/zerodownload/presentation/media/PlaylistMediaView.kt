package io.bashpsk.zerodownload.presentation.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.presentation.components.LabelRow
import io.bashpsk.emptylibs.formatter.format.DurationPattern
import io.bashpsk.emptylibs.formatter.format.duration
import io.bashpsk.emptylibs.formatter.format.findAspectRatio
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration.Companion.seconds

@Composable
inline fun PlaylistMediaView(
    modifier: Modifier = Modifier,
    mediaData: MediaData = MediaData(),
    isMediaSelect: Boolean = false,
    isSelected: Boolean = false,
    crossinline onMediaClick: (media: MediaData) -> Unit = {}
) {

    val context = LocalContext.current

    val thumbnailRequest = remember(mediaData) {
        ImageRequest.Builder(context = context)
            .coroutineContext(context = Dispatchers.IO)
            .data(data = mediaData.artwork)
            .diskCachePolicy(policy = CachePolicy.ENABLED)
            .diskCacheKey(key = mediaData.artwork)
            .memoryCachePolicy(policy = CachePolicy.ENABLED)
            .memoryCacheKey(key = mediaData.artwork)
            .build()
    }

    val formattedDuration by remember(mediaData) {
        derivedStateOf {
            mediaData.duration.seconds.duration(pattern = DurationPattern.Separator(char = ":"))
        }
    }

    val thumbnailAspectRatio by remember {
        derivedStateOf { findAspectRatio(width = 16.0F, height = 9.0F) }
    }

    val elevatedCardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val elevatedCardShape = MaterialTheme.shapes.extraSmall

    ElevatedCard(
        modifier = modifier,
        shape = elevatedCardShape,
        colors = elevatedCardColors,
        onClick = {

            onMediaClick(mediaData)
        }
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 0.dp)
            ) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = thumbnailAspectRatio)
                        .clip(shape = elevatedCardShape),
                    model = thumbnailRequest,
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.thumbnail)
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 1.dp),
                    text = mediaData.title,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 1.dp, end = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LabelRow(
                        modifier = Modifier.weight(weight = 2.0F),
                        icon = Icons.Filled.AccountCircle,
                        text = mediaData.channelName
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.AccessTime,
                        text = formattedDuration
                    )
                }
            }

            if (isMediaSelect) Checkbox(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(alignment = Alignment.TopEnd),
                checked = isSelected,
                onCheckedChange = null
            )
        }
    }
}