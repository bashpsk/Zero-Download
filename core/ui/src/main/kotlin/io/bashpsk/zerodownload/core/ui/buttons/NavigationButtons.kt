package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun NavigateBackIconButton(
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.go_back)) {

        IconButton(
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.go_back)
            )
        }
    }
}

@Composable
fun NavigateMenuIconButton(
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.menu)) {

        IconButton(
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = stringResource(R.string.menu)
            )
        }
    }
}