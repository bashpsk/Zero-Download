package io.bashpsk.zerodownload.core.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun DownloadSmallFab(expanded: Boolean = true, onClick: () -> Unit) {

    SmallExtendedFloatingActionButton(
        text = {

            Text(text = stringResource(R.string.download))
        },
        icon = {

            Icon(
                imageVector = Icons.Filled.FileDownload,
                contentDescription = stringResource(R.string.download)
            )
        },
        expanded = expanded,
        onClick = onClick
    )
}