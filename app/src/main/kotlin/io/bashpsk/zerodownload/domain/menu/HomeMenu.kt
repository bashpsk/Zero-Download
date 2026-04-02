package io.bashpsk.zerodownload.domain.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.bashpsk.zerodownload.R

enum class HomeMenu(val label: Int, val icon: ImageVector) {

    AppTheme(label = R.string.app_theme, icon = Icons.Filled.Brightness6);

    companion object {

        @Composable
        fun HomeMenu.toLabel(): String {

            return stringResource(label)
        }
    }
}