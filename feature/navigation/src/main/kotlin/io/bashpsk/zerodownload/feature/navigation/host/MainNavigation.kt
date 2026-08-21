package io.bashpsk.zerodownload.feature.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Composable
fun MainNavigation(navBackStack: NavBackStack<NavKey>) {

    MainNavHost(navBackStack = navBackStack)
}