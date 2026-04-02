package io.bashpsk.zerodownload.presentation.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Stable
val Dp.toPixels: Float
    @ReadOnlyComposable
    @Composable
    get() = with(receiver = LocalDensity.current) { this@toPixels.toPx() }