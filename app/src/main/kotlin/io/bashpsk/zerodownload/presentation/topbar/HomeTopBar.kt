package io.bashpsk.zerodownload.presentation.topbar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.datastore.PreferenceData
import io.bashpsk.zerodownload.domain.menu.HomeMenu
import io.bashpsk.zerodownload.domain.menu.HomeMenu.Companion.toLabel
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.emptylibs.datastoreui.preference.ListOptionMenuPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun HomeTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior?,
    isOptionMenu: Boolean,
    isMediaSelect: Boolean,
    crossinline onOptionMenu: (isVisible: Boolean) -> Unit,
    crossinline onMediaSelect: (isVisible: Boolean) -> Unit,
    noinline onDownloadClick: () -> Unit,
    crossinline onNavigateScreen: (navScreen: NavScreen) -> Unit
) {

    TopAppBar(
        modifier = modifier,
        title = {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.home_app_title),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {

            when (isMediaSelect) {

                true -> IconButton(onClick = { onMediaSelect(false) }) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }

                false -> IconButton(onClick = { onNavigateScreen(NavScreen.AppSettings) }) {

                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings_app_title)
                    )
                }
            }
        },
        actions = {

            if (isMediaSelect) FilledTonalButton(onClick = onDownloadClick) {

                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = stringResource(R.string.download)
                )

                Spacer(modifier = Modifier.width(width = 2.dp))

                Text(
                    text = stringResource(R.string.download),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = { onOptionMenu(true) }) {

                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.more_option)
                )
            }

            DropdownMenu(
                expanded = isOptionMenu,
                onDismissRequest = { onOptionMenu(false) },
                containerColor = MenuDefaults.containerColor
            ) {

                HorizontalDivider()

                ListOptionMenuPreference(
                    key = PreferenceData.ApplicationTheme.key,
                    initialValue = PreferenceData.ApplicationTheme.initial,
                    entities = PreferenceData.ApplicationTheme.entities,
                    title = HomeMenu.AppTheme.toLabel(),
                    leadingContent = {

                        Icon(
                            imageVector = HomeMenu.AppTheme.icon,
                            contentDescription = HomeMenu.AppTheme.toLabel()
                        )
                    },
                    onMenuDismiss = { onOptionMenu(false) }
                )

                HorizontalDivider()
            }
        },
        scrollBehavior = scrollBehavior
    )
}