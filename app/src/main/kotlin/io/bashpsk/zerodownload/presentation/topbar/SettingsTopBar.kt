package io.bashpsk.zerodownload.presentation.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.topbar.TopAppBarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    topAppBarType: TopAppBarType,
    onNavigationClick: () -> Unit
) {

    GeneralTopBar(
        modifier = modifier,
        title = stringResource(R.string.settings_app_title),
        topAppBarType = topAppBarType,
        scrollBehavior = scrollBehavior,
        onNavigationClick = onNavigationClick
    )
}