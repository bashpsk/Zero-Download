package io.bashpsk.zerodownload.data.extension

import android.content.Context
import io.bashpsk.zerodownload.domain.settings.AppLanguage
import java.util.Locale

fun Context.updateLanguage(language: AppLanguage) {

    val config = resources.configuration
    val locale = if (language.code.isNotEmpty()) Locale(language.code) else Locale.getDefault()

    Locale.setDefault(locale)
    config.setLocale(locale)
    createConfigurationContext(config)
    resources.updateConfiguration(config, resources.displayMetrics)
}