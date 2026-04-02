package io.bashpsk.zerodownload.domain.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

enum class AppTheme(val theme: String) {

    SYSTEM(theme = "System"),
    DARK(theme = "Dark"),
    LIGHT(theme = "Light");

    companion object {

        @Composable
        @ReadOnlyComposable
        fun getTheme(theme: String): Boolean {

            return when (valueOf(value = theme)) {

                SYSTEM -> isSystemInDarkTheme()
                DARK -> true
                LIGHT -> false
            }
        }
    }
}