package io.bashpsk.zerodownload.feature.home.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.core.ui.buttons.OptionMenuIconButton
import io.bashpsk.zerodownload.core.ui.buttons.SettingsIconButton
import io.bashpsk.zerodownload.core.ui.menu.ApplicationThemeMenuPreference
import io.bashpsk.zerodownload.core.ui.topbar.SearchTopBar
import io.bashpsk.zerodownload.feature.home.R
import io.bashpsk.zerodownload.feature.home.event.HomeUIEvent

@PublishedApi
@Composable
internal inline fun HomeTopBar(
    modifier: Modifier = Modifier,
    isOptionMenu: Boolean,
    crossinline onNavigateScreen: (navScreen: NavScreen) -> Unit,
    crossinline onUIEvent: (uiEvent: HomeUIEvent) -> Unit,
    scrollBehavior: SearchBarScrollBehavior?
) {

    SearchTopBar(
        modifier = modifier.fillMaxWidth(),
        placeholder = stringResource(R.string.home_screen),
        scrollBehavior = scrollBehavior,
        onSearch = { query ->

            onUIEvent(HomeUIEvent.MediaSearch(link = query))
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Uri
        ),
        navigationIcon = {

            SettingsIconButton(onClick = { onNavigateScreen(NavScreen.AppSettings) })
        },
        actions = {

            OptionMenuIconButton(onClick = { onUIEvent(HomeUIEvent.OptionMenu(isVisible = true)) })

            DropdownMenu(
                expanded = isOptionMenu,
                onDismissRequest = { onUIEvent(HomeUIEvent.OptionMenu(isVisible = false)) }
            ) {

                HorizontalDivider()

                ApplicationThemeMenuPreference(
                    onMenuDismiss = { onUIEvent(HomeUIEvent.OptionMenu(isVisible = false)) }
                )

                HorizontalDivider()
            }
        }
    )
}