package io.bashpsk.zerodownload.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavigation(navBackStack: NavBackStack<NavKey>) {

    MainNavHost(navBackStack = navBackStack)
}