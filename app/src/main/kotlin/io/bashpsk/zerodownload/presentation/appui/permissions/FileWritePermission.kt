package io.bashpsk.zerodownload.presentation.appui.permissions

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
inline fun FileWritePermission(
    modifier: Modifier = Modifier,
    visibleState: Boolean,
    crossinline onPermissionResult: (result: Boolean) -> Unit = {}
) {

    AnimatedVisibility(visible = visibleState) {

        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> ManageStoragePermission(
                modifier = modifier,
                onPermissionResult = onPermissionResult
            )

            else -> WriteStoragePermission(
                modifier = modifier,
                onPermissionResult = onPermissionResult
            )
        }
    }
}