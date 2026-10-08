package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun MediaPlayOperationButton(onClick: () -> Unit) {

    FilledTonalButton(
        shapes = ButtonDefaults.shapes(),
        onClick = onClick
    ) {

        ButtonIconText(
            icon = Icons.Filled.PlayArrow,
            text = stringResource(R.string.play)
        )
    }
}

@Composable
fun MediaDownloadOperationButton(onClick: () -> Unit) {

    ElevatedButton(
        shapes = ButtonDefaults.shapes(),
        onClick = onClick
    ) {

        ButtonIconText(
            icon = Icons.Filled.Download,
            text = stringResource(R.string.download)
        )
    }
}