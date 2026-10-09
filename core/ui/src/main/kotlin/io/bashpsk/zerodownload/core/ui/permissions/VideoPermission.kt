package io.bashpsk.zerodownload.core.ui.permissions

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VideoPermission(
    modifier: Modifier = Modifier,
    visibleState: Boolean,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    AnimatedVisibility(visible = visibleState) {

        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> ReadMediaVideoPermission(
                modifier = modifier,
                onPermissionResult = onPermissionResult
            )

            else -> ReadStoragePermission(
                modifier = modifier,
                onPermissionResult = onPermissionResult
            )
        }
    }
}