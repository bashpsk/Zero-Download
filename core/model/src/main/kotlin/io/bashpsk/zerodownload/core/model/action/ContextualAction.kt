package io.bashpsk.zerodownload.core.model.action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
sealed class ContextualAction(open val enabled: Boolean, open val icon: ImageVector) {

    data class More(
        override val enabled: Boolean = true,
        override val icon: ImageVector = Icons.Filled.MoreVert
    ) : ContextualAction(icon = icon, enabled = enabled)

    data class SelectAll(
        override val enabled: Boolean = true,
        override val icon: ImageVector = Icons.Filled.SelectAll
    ) : ContextualAction(icon = icon, enabled = enabled)

    data class SelectNone(
        override val enabled: Boolean = true,
        override val icon: ImageVector = Icons.Filled.Deselect
    ) : ContextualAction(icon = icon, enabled = enabled)
}