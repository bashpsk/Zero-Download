package io.bashpsk.zerodownload.presentation.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.extension.getIcon
import io.bashpsk.zerodownload.domain.media.PlaylistMediaData
import io.bashpsk.zerodownload.presentation.components.LabelRow
import io.bashpsk.zerodownload.ytdlext.extract.ExtractorType

@Composable
fun PlaylistView(
    modifier: Modifier = Modifier,
    playlistData: PlaylistMediaData = PlaylistMediaData()
) {

    val extractorType by remember(playlistData) {
        derivedStateOf { ExtractorType.find(name = playlistData.extractorKey) }
    }

    val extractorImage by remember(extractorType) { derivedStateOf { extractorType.getIcon() } }

    var isPlaylistExpanded by rememberSaveable { mutableStateOf(false) }

    val elevatedCardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val elevatedCardShape = MaterialTheme.shapes.extraSmall

    ElevatedCard(
        modifier = modifier,
        shape = elevatedCardShape,
        colors = elevatedCardColors,
        onClick = { isPlaylistExpanded = !isPlaylistExpanded }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(size = 48.dp),
                imageVector = Icons.Filled.VideoLibrary,
                contentDescription = stringResource(R.string.thumbnail)
            )

            Column(
                modifier = Modifier.weight(weight = 1.0F),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = playlistData.title,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LabelRow(
                        modifier = Modifier.weight(weight = 2.0F),
                        icon = Icons.Filled.AccountCircle,
                        text = playlistData.channelName
                    )

                    LabelRow(
                        modifier = Modifier.weight(weight = 1.0F),
                        image = painterResource(extractorImage),
                        text = extractorType.label
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${playlistData.mediaList.size} ${stringResource(R.string.items)}",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium
                )

                if (isPlaylistExpanded) Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${stringResource(R.string.description)}:\n${playlistData.description}",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Icon(
                modifier = Modifier.align(alignment = Alignment.Bottom),
                imageVector = when (isPlaylistExpanded) {

                    true -> Icons.Filled.ArrowDropUp
                    false -> Icons.Filled.ArrowDropDown
                },
                contentDescription = stringResource(R.string.expanded)
            )
        }
    }
}