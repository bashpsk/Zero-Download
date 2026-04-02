package io.bash_psk.empty_layer.presentation.appui.permissions

import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import io.bashpsk.zerodownload.presentation.appui.permissions.FileReadPermission
import io.bashpsk.zerodownload.presentation.appui.permissions.FileWritePermission

inline fun LazyGridScope.screenFileReadWritePermissionItems(
    manageStoragePermissionVisible: Boolean,
    readStoragePermissionVisible: Boolean,
    writeStoragePermissionVisible: Boolean,
    crossinline onPermissionResult: (result: Boolean) -> Unit = {}
) {

    item(span = { GridItemSpan(currentLineSpan = maxLineSpan) }) {

        FileReadPermission(
            modifier = Modifier.animateItem(
                fadeInSpec = tween(durationMillis = 250),
                fadeOutSpec = tween(durationMillis = 100),
                placementSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            ),
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
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(durationMillis = 250),
                    fadeOutSpec = tween(durationMillis = 100),
                    placementSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ),
                visibleState = writeStoragePermissionVisible,
                onPermissionResult = onPermissionResult
            )
        }
    }
}