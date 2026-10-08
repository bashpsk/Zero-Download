package io.bashpsk.zerodownload.core.ui.permissions

import android.os.Build
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import io.bashpsk.zerodownload.core.ui.animation.itemBounceAnimation

inline fun LazyGridScope.screenFileReadWritePermissionItems(
    manageStoragePermissionVisible: Boolean,
    readStoragePermissionVisible: Boolean,
    writeStoragePermissionVisible: Boolean,
    crossinline onPermissionResult: (result: Boolean) -> Unit = {}
) {

    item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

        FileReadPermission(
            modifier = Modifier.itemBounceAnimation(),
            visibleState = when {

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> manageStoragePermissionVisible
                else -> readStoragePermissionVisible
            },
            onPermissionResult = onPermissionResult
        )
    }

    when {

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {}

        else -> item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

            FileWritePermission(
                modifier = Modifier.itemBounceAnimation(),
                visibleState = writeStoragePermissionVisible,
                onPermissionResult = onPermissionResult
            )
        }
    }
}