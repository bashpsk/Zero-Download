package io.bashpsk.zerodownload.core.ui.animation

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.ui.Modifier
import io.bashpsk.zerodownload.core.model.media.EmptyMediaType
import io.bashpsk.zerodownload.core.ui.media.EmptyMediaView

fun LazyGridScope.EmptyMediaAnimation(
    isEmptyMediaView: Boolean?,
    emptyMediaType: EmptyMediaType
) {

    item(
        key = "EmptyMediaView",
        contentType = "EmptyMediaView"
    ) {

        EmptyMediaView(
            modifier = Modifier.itemBounceAnimation(),
            isEmptyMediaView = isEmptyMediaView,
            emptyMediaType = emptyMediaType
        )
    }
}

fun LazyStaggeredGridScope.EmptyMediaAnimation(
    isEmptyMediaView: Boolean?,
    emptyMediaType: EmptyMediaType
) {

    item(
        key = "EmptyMediaView",
        contentType = "EmptyMediaView"
    ) {

        EmptyMediaView(
            modifier = Modifier.itemBounceAnimation(),
            isEmptyMediaView = isEmptyMediaView,
            emptyMediaType = emptyMediaType
        )
    }
}

fun LazyListScope.EmptyMediaAnimation(
    isEmptyMediaView: Boolean?,
    emptyMediaType: EmptyMediaType
) {

    item(
        key = "EmptyMediaView",
        contentType = "EmptyMediaView"
    ) {

        EmptyMediaView(
            modifier = Modifier.itemBounceAnimation(),
            isEmptyMediaView = isEmptyMediaView,
            emptyMediaType = emptyMediaType
        )
    }
}