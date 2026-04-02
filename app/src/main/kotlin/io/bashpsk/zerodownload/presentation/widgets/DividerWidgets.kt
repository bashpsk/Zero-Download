package io.bashpsk.zerodownload.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleHorizontalDivider(modifier: Modifier = Modifier) {

    HorizontalDivider(
        modifier = modifier.fillMaxWidth(fraction = 0.70F),
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.primaryFixedDim
    )
}

@Composable
fun EndHorizontalDivider(modifier: Modifier = Modifier) {

    HorizontalDivider(
        modifier = modifier.fillMaxWidth(fraction = 1.0F),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.tertiaryFixedDim
    )
}