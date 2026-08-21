package io.bashpsk.zerodownload.core.ui.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.core.model.topbar.TopAppBarType
import io.bashpsk.zerodownload.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloaderSettingTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    topAppBarType: TopAppBarType,
    onNavigationClick: () -> Unit
) {

    GeneralTopBar(
        modifier = modifier,
        title = stringResource(R.string.downloader_settings),
        topAppBarType = topAppBarType,
        scrollBehavior = scrollBehavior,
        onNavigationClick = onNavigationClick
    )
}