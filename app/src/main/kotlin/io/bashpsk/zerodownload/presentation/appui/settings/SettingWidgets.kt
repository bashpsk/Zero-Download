package io.bashpsk.zerodownload.presentation.appui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.zerodownload.domain.datastore.PreferenceData
import io.bashpsk.zerodownload.domain.datastore.PreferenceData.Companion.toSummary
import io.bashpsk.zerodownload.domain.datastore.PreferenceData.Companion.toTitle
import io.bashpsk.zerodownload.domain.settings.AppLanguage
import io.bashpsk.zerodownload.presentation.settings.SettingView
import io.bashpsk.emptylibs.datastoreui.datastore.LocalDatastore
import io.bashpsk.emptylibs.datastoreui.extension.getPreference
import io.bashpsk.emptylibs.datastoreui.preference.CardPreference
import io.bashpsk.emptylibs.datastoreui.preference.DropDownPreference
import io.bashpsk.emptylibs.datastoreui.preference.ListOptionPreference
import io.bashpsk.emptylibs.datastoreui.preference.SwitchPreference

@Composable
fun ApplicationThemeSetting(modifier: Modifier = Modifier) {

    SettingView {

        DropDownPreference(
            modifier = modifier,
            key = PreferenceData.ApplicationTheme.key,
            initialValue = PreferenceData.ApplicationTheme.initial,
            entities = PreferenceData.ApplicationTheme.entities,
            title = PreferenceData.ApplicationTheme.toTitle(),
            summary = PreferenceData.ApplicationTheme.toSummary(),
        )
    }
}

@Composable
fun DynamicColorThemeSetting(modifier: Modifier = Modifier) {

    SettingView {

        SwitchPreference(
            modifier = modifier,
            key = PreferenceData.DynamicColorTheme.key,
            initialValue = PreferenceData.DynamicColorTheme.initial,
            title = PreferenceData.DynamicColorTheme.toTitle(),
            summary = PreferenceData.DynamicColorTheme.toSummary()
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
            key = PreferenceData.ApplicationLanguage.key,
            initialValue = PreferenceData.ApplicationLanguage.initial,
            title = PreferenceData.ApplicationLanguage.toTitle(),
            summary = summary,
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
            title = PreferenceData.YtDlLibrary.toTitle(),
            summary = summary,
            onClick = onUpdateYtDl
        )
    }
}