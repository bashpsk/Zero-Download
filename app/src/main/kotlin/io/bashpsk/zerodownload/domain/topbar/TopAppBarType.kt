package io.bashpsk.zerodownload.domain.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopAppBarType(val icon: ImageVector) {

    Menu(icon = Icons.Filled.Menu),
    Arrow(icon = Icons.AutoMirrored.Filled.ArrowBack),
}