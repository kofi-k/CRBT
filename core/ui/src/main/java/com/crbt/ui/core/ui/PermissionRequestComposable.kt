package com.crbt.ui.core.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PermissionRequestComposable(
    onPermissionsGranted: () -> Unit
) {

    val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.all { it.value }) {
            onPermissionsGranted()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions)
    }
}