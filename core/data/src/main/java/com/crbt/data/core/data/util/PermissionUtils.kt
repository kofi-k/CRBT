package com.crbt.data.core.data.util


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtils {

    fun checkAndRequestPermissions(
        context: Context,
        permissions: Array<String>,
        requestCode: Int,
        onPermissionsGranted: () -> Unit
    ): Boolean {
        val permissionsNeeded = permissions.filter {
            ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsNeeded.toTypedArray(),
                requestCode
            )
            false
        } else {
            onPermissionsGranted()
            true
        }
    }
}

