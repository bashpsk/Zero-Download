package io.bashpsk.zerodownload.domain.map

import io.bashpsk.zerodownload.domain.settings.AppLanguage
import io.bashpsk.zerodownload.domain.settings.AppTheme
import io.bashpsk.zerodownload.domain.settings.MediaSort

object PreferenceMap {

    val MEDIA_SORT_MAP = MediaSort.entries.associate { mediaSort ->

        mediaSort.label to mediaSort.name
    }

    val APP_THEME_MAP = AppTheme.entries.associate { theme ->

        theme.theme to theme.name
    }

    val APP_LANGUAGE_MAP = AppLanguage.entries.associate { language ->

        language.language to language.name
    }
}