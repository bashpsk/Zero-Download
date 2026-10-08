package io.bashpsk.zerodownload.core.ui.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MenuItemView(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: MenuItemColors = MenuDefaults.itemColors()
) {

    MenuItemView(
        modifier = modifier,
        enabled = enabled,
        text = text,
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = text) },
        onClick = onClick,
        colors = colors
    )
}

@Composable
fun MenuItemView(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    colors: MenuItemColors = MenuDefaults.itemColors()
) {

    DropdownMenuItem(
        modifier = modifier,
        enabled = enabled,
        text = {

            Text(
                text = text,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
        colors = colors
    )
}