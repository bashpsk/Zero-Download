package io.bashpsk.zerodownload.domain.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class AppTheme(val theme: String) {

    SYSTEM(theme = "System"),
    DARK(theme = "Dark"),
    LIGHT(theme = "Light");

    companion object {

        @Composable
        fun getTheme(theme: String): Boolean {

            return when (valueOf(value = theme)) {

                SYSTEM -> isSystemInDarkTheme()
                DARK -> true
                LIGHT -> false
            }
        }
    }
}