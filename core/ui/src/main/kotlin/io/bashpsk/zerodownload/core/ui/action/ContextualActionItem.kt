package io.bashpsk.zerodownload.core.ui.action

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.core.model.action.ContextualAction
import io.bashpsk.zerodownload.core.ui.R
import io.bashpsk.zerodownload.core.ui.menu.MenuItemView

@Composable
fun ContextualActionBarItem(
    modifier: Modifier = Modifier,
    optionData: ContextualAction,
    onClick: () -> Unit = {}
) {

    val cardColors = CardDefaults.cardColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )

    Card(
        modifier = modifier,
        enabled = optionData.enabled,
        shape = MaterialTheme.shapes.extraSmall,
        colors = cardColors,
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.size(size = 20.dp),
                imageVector = optionData.icon,
                contentDescription = optionData.getLabel()
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            Text(
                text = optionData.getLabel(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ContextualActionMenuItem(
    modifier: Modifier = Modifier,
    optionData: ContextualAction,
    onClick: () -> Unit = {}
) {

    MenuItemView(
        modifier = modifier,
        enabled = optionData.enabled,
        text = optionData.getLabel(),
        leadingIcon = optionData.icon,
        onClick = onClick
    )
}

@ReadOnlyComposable
@Composable
private fun ContextualAction.getLabel(): String {

    val id = when (this) {

        is ContextualAction.More -> R.string.more_action
        is ContextualAction.SelectAll -> R.string.select_all_action
        is ContextualAction.SelectNone -> R.string.select_none_action
    }

    return stringResource(id)
}