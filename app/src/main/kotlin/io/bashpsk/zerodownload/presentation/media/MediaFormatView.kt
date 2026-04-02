package io.bashpsk.zerodownload.presentation.media

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.domain.media.MediaFormatData
import io.bashpsk.zerodownload.presentation.components.LabelRow

@Composable
inline fun MediaFormatView(
    modifier: Modifier = Modifier,
    mediaFormat: MediaFormatData = MediaFormatData.Unknown(),
    isMediaSelect: Boolean = false,
    isSelected: Boolean = false,
    crossinline onMediaClick: (media: MediaFormatData) -> Unit = {},
    crossinline onMediaLongClick: (media: MediaFormatData) -> Unit = {}
) {

    val elevatedCardContainerColor = when (mediaFormat) {

        is MediaFormatData.VideoAndAudio -> MaterialTheme.colorScheme.tertiaryContainer
        is MediaFormatData.VideoOnly -> MaterialTheme.colorScheme.secondaryContainer
        is MediaFormatData.AudioOnly -> MaterialTheme.colorScheme.primaryContainer
        is MediaFormatData.Unknown -> MaterialTheme.colorScheme.errorContainer
    }

    val elevatedCardColors = CardDefaults.elevatedCardColors(
        containerColor = elevatedCardContainerColor,
        contentColor = contentColorFor(elevatedCardContainerColor)
    )

    val cardShape = MaterialTheme.shapes.extraSmall

    val clickableModifier = Modifier.combinedClickable(
        onClick = {

            when (isMediaSelect) {

                true -> onMediaLongClick(mediaFormat)
                false -> onMediaClick(mediaFormat)
            }
        },
        onLongClick = {

            onMediaLongClick(mediaFormat)
        }
    )

    ElevatedCard(
        modifier = modifier.then(clickableModifier),
        shape = cardShape,
        colors = elevatedCardColors
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = mediaFormat.formatLabel,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis
                )

                LabelRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Ext & ID",
                    text = "${mediaFormat.ext} & ${mediaFormat.formatId}"
                )

                LabelRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Codec",
                    text = mediaFormat.codec
                )

                LabelRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Quality",
                    text = mediaFormat.qualityLabel
                )

                LabelRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Size",
                    text = mediaFormat.fileSize
                )
            }

            if (isMediaSelect) Checkbox(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(all = 4.dp),
                checked = isSelected,
                onCheckedChange = null
            )
        }
    }
}