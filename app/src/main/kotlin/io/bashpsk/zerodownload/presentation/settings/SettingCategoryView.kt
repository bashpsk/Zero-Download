package io.bashpsk.zerodownload.presentation.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.navigation.AppSettingCategory
import io.bashpsk.zerodownload.domain.navigation.AppSettingCategory.Companion.toDescription
import io.bashpsk.zerodownload.domain.navigation.AppSettingCategory.Companion.toTitle
import io.bashpsk.zerodownload.domain.navigation.NavScreen
import io.bashpsk.emptylibs.datastoreui.preference.CardPreference

@Composable
inline fun SettingCategoryView(
    modifier: Modifier = Modifier,
    settingCategory: AppSettingCategory,
    crossinline onOpenSettings: (navScreen: NavScreen) -> Unit
) {

    CardPreference(
        modifier = modifier,
        title = settingCategory.toTitle(),
        summary = settingCategory.toDescription(),
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