package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun ConfirmationButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    Button(
        enabled = enabled,
        shapes = ButtonDefaults.shapes(),
        onClick = onClick
    ) {

        ButtonIconText(
            icon = Icons.Filled.Done,
            text = stringResource(R.string.confirm)
        )
    }
}

@Composable
fun CreateConfirmationButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    Button(
        enabled = enabled,
        shapes = ButtonDefaults.shapes(),
        onClick = onClick
    ) {

        ButtonIconText(
            icon = Icons.Filled.Done,
            text = stringResource(R.string.create)
        )
    }
}

@Composable
fun ConfirmationIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {

    ButtonTooltipBox(tooltip = stringResource(R.string.confirm)) {

        IconButton(
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.DoneAll,
                contentDescription = stringResource(R.string.confirm)
            )
        }
    }
}