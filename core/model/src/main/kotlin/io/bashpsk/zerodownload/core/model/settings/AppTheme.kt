package io.bashpsk.zerodownload.core.model.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable

enum class AppTheme(val theme: String) {

    SYSTEM(theme = "System"),
    DARK(theme = "Dark"),
    LIGHT(theme = "Light");

    companion object {

        @Stable
        @ReadOnlyComposable
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