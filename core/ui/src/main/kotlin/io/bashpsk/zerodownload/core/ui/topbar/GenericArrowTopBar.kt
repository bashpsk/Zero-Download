package io.bashpsk.zerodownload.core.ui.topbar

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.bashpsk.zerodownload.core.ui.buttons.NavigateBackIconButton

@Composable
fun GenericArrowTopBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        title = {

            Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {

            NavigateBackIconButton(onClick = onNavigationClick)
        },
        scrollBehavior = scrollBehavior
    )
}