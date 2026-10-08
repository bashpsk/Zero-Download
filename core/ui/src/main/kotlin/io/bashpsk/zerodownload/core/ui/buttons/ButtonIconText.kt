package io.bashpsk.zerodownload.core.ui.buttons

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RowScope.ButtonIconText(icon: ImageVector, text: String) {

    Icon(
        modifier = Modifier.size(ButtonDefaults.IconSize),
        imageVector = icon,
        contentDescription = text
    )

    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

    Text(
        text = text,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}