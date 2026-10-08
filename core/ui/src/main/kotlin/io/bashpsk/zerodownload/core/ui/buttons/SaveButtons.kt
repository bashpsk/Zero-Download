package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun SaveIconButton(onClick: () -> Unit) {

    ButtonTooltipBox(tooltip = stringResource(R.string.save)) {

        IconButton(
            shapes = IconButtonDefaults.shapes(),
            onClick = onClick
        ) {

            Icon(
                imageVector = Icons.Filled.SaveAs,
                contentDescription = stringResource(R.string.save)
            )
        }
    }
}