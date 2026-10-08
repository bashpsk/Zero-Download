package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun SettingsIconButton(onClick: () -> Unit) {

    IconButton(
        shapes = IconButtonDefaults.shapes(),
        onClick = onClick
    ) {

        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings)
        )
    }
}