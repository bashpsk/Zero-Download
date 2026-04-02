package io.bashpsk.zerodownload.presentation.appui.permissions

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.bashpsk.zerodownload.domain.media.MediaDataType

@Composable
inline fun MediaWritePermission(
    modifier: Modifier = Modifier,
    visibleState:  Boolean,
    mediaDataType: MediaDataType,
    crossinline onPermissionResult: (result: Boolean) -> Unit = {}
) {

    AnimatedVisibility(visible = visibleState) {

        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> when (mediaDataType){

                MediaDataType.Audio -> ReadMediaAudioPermission(
                    modifier = modifier,
                    onPermissionResult = onPermissionResult
                )

                MediaDataType.Image -> ReadStoragePermission(
                    modifier = modifier,
                    onPermissionResult = onPermissionResult
                )

                MediaDataType.Video -> ReadMediaVideoPermission(
                    modifier = modifier,
                    onPermissionResult = onPermissionResult
                )

                MediaDataType.Unknown -> ReadStoragePermission(
                    modifier = modifier,
                    onPermissionResult = onPermissionResult
                )
            }

            else -> WriteStoragePermission(
                modifier = modifier,
                onPermissionResult = onPermissionResult
            )
        }
    }
}