package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun PlayerSeekForwardIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.seek_backward)) {

        IconButton(
            modifier = modifier,
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.KeyboardDoubleArrowLeft,
                contentDescription = stringResource(R.string.seek_backward)
            )
        }
    }
}

@Composable
fun PlayerSeekBackwardIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.seek_forward)) {

        IconButton(
            modifier = modifier,
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                contentDescription = stringResource(R.string.seek_forward)
            )
        }
    }
}