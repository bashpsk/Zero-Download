package io.bashpsk.zerodownload.core.navigation.extension

import io.bashpsk.zerodownload.core.navigation.screen.NavScreen
import kotlinx.collections.immutable.persistentListOf

fun findNavScreen(uri: String, type: String): NavScreen? {

    return when {

        persistentListOf("text/plain").contains(element = type) -> NavScreen.Home
        else -> NavScreen.Home
    }
}

fun findNavScreen(id: Int): NavScreen {

    return when (id) {

        0 -> NavScreen.Home
        1 -> NavScreen.Downloads
        2 -> NavScreen.Command
        3 -> NavScreen.Settings
        4 -> NavScreen.About
        else -> NavScreen.Home
    }
}