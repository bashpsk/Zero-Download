package io.bashpsk.zerodownload.core.datastore.map

import io.bashpsk.zerodownload.core.model.settings.AppLanguage
import io.bashpsk.zerodownload.core.model.settings.AppTheme
import io.bashpsk.zerodownload.core.model.settings.MediaSort
import kotlinx.collections.immutable.toImmutableMap

object PreferenceMap {

    val APP_THEME_MAP = AppTheme.entries.associate { theme ->

        theme.theme to theme.name
    }.toImmutableMap()

    val APP_LANGUAGE_MAP = AppLanguage.entries.associate { language ->

        language.language to language.name
    }.toImmutableMap()

    val MEDIA_SORT_MAP = MediaSort.entries.associate { mediaSort ->

        mediaSort.label to mediaSort.name
    }.toImmutableMap()
}