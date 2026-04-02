package io.bashpsk.zerodownload.presentation.media

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.extension.getIcon
import io.bashpsk.zerodownload.domain.media.EmptyMediaType
import io.bashpsk.zerodownload.domain.media.MediaData
import io.bashpsk.zerodownload.presentation.components.LabelRow
import io.bashpsk.zerodownload.ytdlext.extract.ExtractorType
import io.bashpsk.emptylibs.formatter.format.DurationPattern
import io.bashpsk.emptylibs.formatter.format.duration
import io.bashpsk.emptylibs.formatter.format.findAspectRatio
import kotlinx.coroutines.Dispatchers
import kotlin.time.Duration.Companion.seconds

@Composable
inline fun MediaView(
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

    val formattedViewCount by remember(mediaData) {
        derivedStateOf { mediaData.viewCount.toString() }
    }

    val formattedRepostCount by remember(mediaData) {
        derivedStateOf { mediaData.repostCount.toString() }
    }

    val formattedLikeCount by remember(mediaData) {
        derivedStateOf { mediaData.likeCount.toString() }
    }

    val formattedDisLikeCount by remember(mediaData) {
        derivedStateOf { mediaData.dislikeCount.toString() }
    }

    val thumbnailAspectRatio by remember {
        derivedStateOf { findAspectRatio(width = 16.0F, height = 9.0F) }
    }

    val extractorType by remember(mediaData) {
        derivedStateOf { ExtractorType.find(name = mediaData.extractorKey) }
    }

    val extractorImage by remember(extractorType) { derivedStateOf { extractorType.getIcon() } }

    val isEmptyMediaView by remember(mediaData) {
        derivedStateOf { mediaData.title.isEmpty() || mediaData.artwork.isEmpty() }
    }

    val elevatedCardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val elevatedCardBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45F)
    val elevatedCardShape = MaterialTheme.shapes.extraSmall

    when (isEmptyMediaView) {

        true -> EmptyMediaView(
            modifier = modifier,
            isEmptyMediaView = true,
            emptyMediaType = EmptyMediaType.LARGE
        )

        false -> ElevatedCard(
            modifier = modifier
                .border(width = 0.6.dp, color = elevatedCardBorderColor, shape = elevatedCardShape)
                .clip(shape = elevatedCardShape),
            shape = elevatedCardShape,
            colors = elevatedCardColors,
            onClick = {

                onMediaClick(mediaData)
            }
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
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

                if (isMediaSelect) Checkbox(
                    modifier = Modifier.align(alignment = Alignment.TopEnd),
                    checked = isSelected,
                    onCheckedChange = null
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = mediaData.title,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        image = painterResource(extractorImage),
                        text = extractorType.label
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.AccessTime,
                        text = formattedDuration
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.RemoveRedEye,
                        text = formattedViewCount
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.ThumbUp,
                        text = formattedLikeCount
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.ThumbDown,
                        text = formattedDisLikeCount
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        icon = Icons.Filled.Repeat,
                        text = formattedRepostCount
                    )
                }
            }
        }
    }
}