package io.bashpsk.zerodownload.feature.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.emptylibs.datastoreui.component.PreferenceSummary
import io.bashpsk.emptylibs.datastoreui.component.PreferenceTitle
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore
import io.bashpsk.emptylibs.datastoreui.extension.getPreference
import io.bashpsk.emptylibs.datastoreui.preference.CardPreference
import io.bashpsk.emptylibs.datastoreui.preference.DropDownPreference
import io.bashpsk.emptylibs.datastoreui.preference.ListOptionPreference
import io.bashpsk.emptylibs.datastoreui.preference.SwitchPreference
import io.bashpsk.zerodownload.core.datastore.settings.PreferenceData
import io.bashpsk.zerodownload.core.datastore.settings.PreferenceData.Companion.toSummary
import io.bashpsk.zerodownload.core.datastore.settings.PreferenceData.Companion.toTitle
import io.bashpsk.zerodownload.core.model.settings.AppLanguage
import io.bashpsk.zerodownload.core.ui.settings.SettingView

@Composable
fun ApplicationThemeSetting(modifier: Modifier = Modifier) {

    SettingView {

        DropDownPreference(
            modifier = modifier,
            datastore = null,
            key = PreferenceData.ApplicationTheme.key,
            initialValue = PreferenceData.ApplicationTheme.initial,
            entities = PreferenceData.ApplicationTheme.entities,
            title = { PreferenceTitle(title = PreferenceData.ApplicationTheme.toTitle()) },
            summary = { PreferenceSummary(summary = PreferenceData.ApplicationTheme.toSummary()) }
        )
    }
}

@Composable
fun DynamicColorThemeSetting(modifier: Modifier = Modifier) {

    SettingView {

        SwitchPreference(
            modifier = modifier,
            datastore = null,
            key = PreferenceData.DynamicColorTheme.key,
            initialValue = PreferenceData.DynamicColorTheme.initial,
            title = { PreferenceTitle(title = PreferenceData.DynamicColorTheme.toTitle()) },
            summary = { PreferenceSummary(summary = PreferenceData.DynamicColorTheme.toSummary()) }
        )
    }
}

@Composable
fun ApplicationLanguageSetting(modifier: Modifier = Modifier) {

    val datastore = LocalDatastore.current

    val getApplicationLanguage by datastore.getPreference(
        key = PreferenceData.ApplicationLanguage.key,
        initial = PreferenceData.ApplicationLanguage.initial
    ).collectAsStateWithLifecycle(initialValue = PreferenceData.ApplicationLanguage.initial)

    val summary by remember(getApplicationLanguage) {
        derivedStateOf { AppLanguage.find(getApplicationLanguage).language }
    }

    SettingView {

        ListOptionPreference(
            modifier = modifier,
            datastore = null,
            key = PreferenceData.ApplicationLanguage.key,
            initialValue = PreferenceData.ApplicationLanguage.initial,
            title = { PreferenceTitle(title = PreferenceData.ApplicationLanguage.toTitle()) },
            summary = { PreferenceSummary(summary = summary) },
            entities = PreferenceData.ApplicationLanguage.entities
        )
    }
}

@Composable
fun YtDlUpdateSetting(modifier: Modifier = Modifier, onUpdateYtDl: () -> Unit) {

    val datastore = LocalDatastore.current

    val getYtDlLibraryVersion by datastore.getPreference(
        key = PreferenceData.YtDlLibrary.key,
        initial = PreferenceData.YtDlLibrary.initial
    ).collectAsStateWithLifecycle(initialValue = PreferenceData.YtDlLibrary.initial)

    val summary by remember(getYtDlLibraryVersion) { derivedStateOf { getYtDlLibraryVersion } }

    SettingView {

        CardPreference(
            modifier = modifier,
            title = { PreferenceTitle(title = PreferenceData.YtDlLibrary.toTitle()) },
            summary = { PreferenceSummary(summary = summary) },
            onClick = onUpdateYtDl
        )
    }
}