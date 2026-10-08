package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonTooltipBox(
    tooltip: String,
    content: @Composable () -> Unit
) {

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        state = rememberTooltipState(),
        tooltip = { PlainTooltip { Text(text = tooltip) } },
        content = content
    )
}