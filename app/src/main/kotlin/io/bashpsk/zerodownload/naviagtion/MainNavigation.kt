package io.bashpsk.zerodownload.naviagtion

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Composable
fun MainNavigation(navBackStack: NavBackStack<NavKey>) {

    MainNavHost(navBackStack = navBackStack)
}