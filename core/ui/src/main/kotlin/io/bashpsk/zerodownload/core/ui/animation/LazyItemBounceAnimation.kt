package io.bashpsk.zerodownload.core.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Stable
context(scope: LazyItemScope)
fun Modifier.itemBounceAnimation(): Modifier {

    val animation = with(scope) {
        Modifier.animateItem(
            fadeInSpec = fadeInSpec,
            fadeOutSpec = fadeOutSpec,
            placementSpec = placementSpec
        )
    }

    return this then animation
}

@Stable
context(scope: LazyGridItemScope)
fun Modifier.itemBounceAnimation(): Modifier {

    val animation = with(scope) {
        Modifier.animateItem(
            fadeInSpec = fadeInSpec,
            fadeOutSpec = fadeOutSpec,
            placementSpec = placementSpec
        )
    }

    return this then animation
}

@Stable
context(scope: LazyStaggeredGridItemScope)
fun Modifier.itemBounceAnimation(): Modifier {

    val animation = with(scope) {
        Modifier.animateItem(
            fadeInSpec = fadeInSpec,
            fadeOutSpec = fadeOutSpec,
            placementSpec = placementSpec
        )
    }

    return this then animation
}

private val fadeInSpec = tween<Float>(durationMillis = 200)
private val fadeOutSpec = tween<Float>(durationMillis = 100)
private val placementSpec = spring<IntOffset>(
    stiffness = Spring.StiffnessLow,
    dampingRatio = Spring.DampingRatioLowBouncy
)