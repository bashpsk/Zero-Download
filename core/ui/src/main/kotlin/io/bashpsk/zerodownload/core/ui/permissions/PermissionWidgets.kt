package io.bashpsk.zerodownload.core.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import io.bashpsk.zerodownload.core.ui.R
import io.bashpsk.zerodownload.core.ui.buttons.ButtonIconText

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionView(
    modifier: Modifier = Modifier,
    permission: String,
    description: String,
    actionSetting: String,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    val context = LocalContext.current

    val isManageStorage by remember(permission) {
        derivedStateOf { permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE }
    }

    val manageStoragePermission = rememberManageStoragePermission()

    val onPermissionResultChange = remember<(Boolean) -> Unit> {
        { result ->
            onPermissionResult(result)
        }
    }

    val permissionState = rememberPermissionState(
        permission = permission,
        onPermissionResult = onPermissionResultChange
    )

    val permissionStatusIcon by remember(permissionState) {
        derivedStateOf {
            when (permissionState.status.shouldShowRationale) {

                true -> Icons.Outlined.Settings
                false -> Icons.Outlined.WarningAmber
            }
        }
    }

    val onPermissionRequestClick = remember<() -> Unit>(
        permissionState,
        isManageStorage,
        actionSetting
    ) {
        {
            when (permissionState.status.shouldShowRationale || isManageStorage) {

                true -> Intent(actionSetting).apply {

                    data = "package:${context.packageName}".toUri()
                }.also(context::startActivity)

                false -> permissionState.launchPermissionRequest()
            }
        }
    }

    RetainedEffect(isManageStorage, manageStoragePermission) {

        if (isManageStorage) onPermissionResult(manageStoragePermission)

        onRetire { }
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {

            Text(
                text = description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                shapes = ButtonDefaults.shapes(),
                onClick = onPermissionRequestClick
            ) {

                ButtonIconText(
                    icon = permissionStatusIcon,
                    text = stringResource(R.string.grant_permission)
                )
            }
        }
    }
}

@Composable
fun ReadStoragePermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.READ_EXTERNAL_STORAGE,
        description = stringResource(R.string.read_storage_permission_desc),
        actionSetting = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}

@Composable
fun WriteStoragePermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        description = stringResource(R.string.write_storage_permission_desc),
        actionSetting = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ManageStoragePermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        description = stringResource(R.string.manage_storage_permission_desc),
        actionSetting = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
        onPermissionResult = onPermissionResult
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReadMediaAudioPermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.READ_MEDIA_AUDIO,
        description = stringResource(R.string.read_media_audio_permission_desc),
        actionSetting = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReadMediaImagePermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.READ_MEDIA_IMAGES,
        description = stringResource(R.string.read_media_audio_permission_desc),
        actionSetting = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ReadMediaVideoPermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.READ_MEDIA_VIDEO,
        description = stringResource(R.string.read_media_video_permission_desc),
        actionSetting = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PostNotificationPermission(
    modifier: Modifier = Modifier,
    onPermissionResult: (result: Boolean) -> Unit = {}
) {

    PermissionView(
        modifier = modifier,
        permission = Manifest.permission.POST_NOTIFICATIONS,
        description = stringResource(R.string.post_notifications_permission_desc),
        actionSetting = Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS,
        onPermissionResult = onPermissionResult
    )
}