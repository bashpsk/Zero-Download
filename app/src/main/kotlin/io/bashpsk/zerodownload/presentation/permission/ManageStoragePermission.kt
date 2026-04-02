package io.bashpsk.zerodownload.presentation.permission

import android.os.Build
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun rememberManageStoragePermission(): Boolean {

    val lifecycleOwner = LocalLifecycleOwner.current

    var permissionState by rememberSaveable { mutableStateOf(checkPackageInstallPermission()) }

    DisposableEffect(lifecycleOwner, permissionState) {

        val lifecycleEventObserver = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_RESUME -> {

                    if (!permissionState) permissionState = checkPackageInstallPermission()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer = lifecycleEventObserver)

        onDispose {

            lifecycleOwner.lifecycle.removeObserver(observer = lifecycleEventObserver)
        }
    }

    return permissionState
}

private fun checkPackageInstallPermission(): Boolean {

    return when {

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
        else -> true
    }
}