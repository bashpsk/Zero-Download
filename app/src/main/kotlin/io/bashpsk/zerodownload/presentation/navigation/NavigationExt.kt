package io.bashpsk.zerodownload.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.removeLastSafe(): Boolean? {

    return removeSafe(element = lastOrNull())
}

fun NavBackStack<NavKey>.removeSafe(element: NavKey?): Boolean? {

    return if (size == 1) null else remove(element = element)
}

fun NavBackStack<NavKey>.addSafe(element: NavKey) {

    if (lastOrNull() == element) return
    find { key -> key.hasNavKeyEqual(element = element) }?.let { key -> remove(element = key) }
    add(element = element)
}

fun NavBackStack<NavKey>.hasNavigationItemSelected(element: NavKey): Boolean {

    return lastOrNull()?.hasNavKeyEqual(element = element) ?: false
}

fun NavKey.hasNavKeyEqual(element: NavKey): Boolean {

    return this === element
}