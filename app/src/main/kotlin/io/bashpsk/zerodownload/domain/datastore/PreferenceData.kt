package io.bashpsk.zerodownload.domain.datastore

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.bashpsk.zerodownload.R
import io.bashpsk.zerodownload.domain.map.PreferenceMap
import io.bashpsk.zerodownload.domain.resources.ConstantString
import io.bashpsk.zerodownload.domain.settings.AppLanguage
import io.bashpsk.zerodownload.domain.settings.AppTheme

sealed class PreferenceData<PK, EK, EV>(
    @param:StringRes
    val title: Int,
    @param:StringRes
    val summary: Int,
    val key: Preferences.Key<PK>,
    val initial: PK,
    val entities: Map<EK, EV>
) {

    data object ApplicationTheme : PreferenceData<String, String, String>(
        title = R.string.application_theme_preference_title,
        summary = R.string.application_theme_preference_summary,
        key = stringPreferencesKey("APPLICATION-THEME"),
        initial = AppTheme.SYSTEM.name,
        entities = PreferenceMap.APP_THEME_MAP
    )

    data object DynamicColorTheme : PreferenceData<Boolean, String, String>(
        title = R.string.dynamic_color_theme_preference_title,
        summary = R.string.dynamic_color_theme_preference_summary,
        key = booleanPreferencesKey("DYNAMIC-COLOR-THEME"),
        initial = true,
        entities = mapOf()
    )

    data object ApplicationLanguage : PreferenceData<String, String, String>(
        title = R.string.application_language_preference_title,
        summary = R.string.application_language_preference_summary,
        key = stringPreferencesKey("APPLICATION-LANGUAGE"),
        initial = AppLanguage.System.name,
        entities = PreferenceMap.APP_LANGUAGE_MAP
    )

    data object YtDlLibrary : PreferenceData<String, String, String>(
        title = R.string.yt_dlp_library_preference_title,
        summary = R.string.yt_dlp_library_theme_preference_summary,
        key = stringPreferencesKey("YT-DL-LIBRARY-VERSION"),
        initial = ConstantString.NONE,
        entities = mapOf()
    );

    companion object {

        @Composable
        @ReadOnlyComposable
        fun PreferenceData<*, *, *>.toTitle(): String {

            return stringResource(title)
        }

        @Composable
        @ReadOnlyComposable
        fun PreferenceData<*, *, *>.toSummary(): String {

            return stringResource(summary)
        }
    }
}