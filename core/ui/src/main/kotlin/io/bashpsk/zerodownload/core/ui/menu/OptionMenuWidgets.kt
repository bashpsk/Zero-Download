package io.bashpsk.zerodownload.core.ui.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.bashpsk.emptylibs.datastoreui.component.PreferenceTitle
import io.bashpsk.emptylibs.datastoreui.preference.ListOptionMenuPreference
import io.bashpsk.zerodownload.core.datastore.settings.PreferenceData
import io.bashpsk.zerodownload.core.ui.R

@Composable
fun ApplicationThemeMenuPreference(onMenuDismiss: () -> Unit = {}) {

    ListOptionMenuPreference(
        datastore = null,
        key = PreferenceData.ApplicationTheme.key,
        initialValue = PreferenceData.ApplicationTheme.initial,
        entities = PreferenceData.ApplicationTheme.entities,
        title = { PreferenceTitle(title = stringResource(R.string.option_menu_app_theme)) },
        leadingContent = {

            Icon(
                imageVector = Icons.Filled.Brightness6,
                contentDescription = stringResource(R.string.option_menu_app_theme)
            )
        },
        onMenuDismiss = onMenuDismiss
    )
}