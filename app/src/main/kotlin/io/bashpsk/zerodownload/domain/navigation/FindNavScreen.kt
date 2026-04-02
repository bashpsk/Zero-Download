package io.bashpsk.zerodownload.domain.navigation

import kotlinx.collections.immutable.persistentListOf

fun findNavScreen(uri: String, type: String) : NavScreen? {

    return when {

        persistentListOf("text/plain").contains(element = type) -> NavScreen.Home
        else -> NavScreen.Home
    }
}