package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun ClearIconButton(isVisible: Boolean, onClick: () -> Unit) {

    AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {

        ButtonTooltipBox(tooltip = stringResource(R.string.clear)) {

            IconButton(
                shapes = IconButtonDefaults.shapes(),
                onClick = onClick
            ) {

                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.clear)
                )
            }
        }
    }
}