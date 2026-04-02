package io.bashpsk.zerodownload.presentation.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import io.bashpsk.zerodownload.R
import io.bashpsk.emptylibs.formatter.format.toFileSize

@Composable
fun SelectPathFab(isFabExpanded: Boolean, pathFileSize: Long, onClick: () -> Unit) {

    val context = LocalContext.current

    val formattedPathFileSize by remember(pathFileSize) {
        derivedStateOf { pathFileSize.toFileSize(context = context) }
    }

    ExtendedFloatingActionButton(
        expanded = isFabExpanded,
        icon = {

            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = stringResource(R.string.select_path)
            )
        },
        text = {

            Text(
                text = "${stringResource(R.string.select_path)} [$formattedPathFileSize]",
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onClick = onClick
    )
}