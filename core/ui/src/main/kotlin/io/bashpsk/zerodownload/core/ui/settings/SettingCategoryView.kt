package io.bashpsk.zerodownload.core.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.emptylibs.datastoreui.component.PreferenceSummary
import io.bashpsk.emptylibs.datastoreui.component.PreferenceTitle
import io.bashpsk.emptylibs.datastoreui.preference.CardPreference
import io.bashpsk.zerodownload.core.navigation.model.AppSettingCategory
import io.bashpsk.zerodownload.core.navigation.model.AppSettingCategory.Companion.toDescription
import io.bashpsk.zerodownload.core.navigation.model.AppSettingCategory.Companion.toTitle
import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import io.bashpsk.zerodownload.core.ui.R

@Composable
inline fun SettingCategoryView(
    modifier: Modifier = Modifier,
    settingCategory: AppSettingCategory,
    crossinline onOpenSettings: (navScreen: NavScreen) -> Unit
) {

    CardPreference(
        modifier = modifier,
        title = { PreferenceTitle(title = settingCategory.toTitle()) },
        summary = { PreferenceSummary(summary = settingCategory.toDescription()) },
        onClick = { onOpenSettings(settingCategory.screen) },
        leadingContent = {

            Icon(
                imageVector = settingCategory.icon,
                contentDescription = settingCategory.toTitle()
            )
        },
        trailingContent = {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.open)
            )
        }
    )
}