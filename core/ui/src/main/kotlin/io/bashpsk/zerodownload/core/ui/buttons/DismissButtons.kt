package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun DismissButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    OutlinedButton(
        shapes = ButtonDefaults.shapes(),
        enabled = enabled,
        onClick = onClick
    ) {

        ButtonIconText(
            icon = Icons.Filled.Close,
            text = stringResource(R.string.dismiss)
        )
    }
}

@Composable
fun DismissIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.dismiss)) {

        IconButton(
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.dismiss)
            )
        }
    }
}