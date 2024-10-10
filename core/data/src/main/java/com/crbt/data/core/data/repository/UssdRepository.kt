package com.crbt.data.core.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.romellfudi.ussdlibrary.USSDController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UssdRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ussdController: USSDController
) {

    private val _ussdState = MutableStateFlow<UssdUiState>(UssdUiState.Idle)
    val ussdState: StateFlow<UssdUiState> get() = _ussdState

    @RequiresApi(Build.VERSION_CODES.O)
    fun dialUssdCode(
        ussdCode: String,
        activity: Activity,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        checkAndRequestPermissions(activity)
        _ussdState.value = UssdUiState.Loading

        val map = HashMap<String, HashSet<String>>().apply {
            put("KEY_LOGIN", HashSet(listOf("espere", "waiting", "loading", "esperando")))
            put(
                "KEY_ERROR",
                HashSet(listOf("problema", "problem", "error", "null", "failed", "insufficient"))
            )
        }

        ussdController.callUSSDOverlayInvoke(ussdCode, map, object : USSDController.CallbackInvoke {
            override fun responseInvoke(message: String) {
                Log.d("USSD_INVOKE", message)

                if (map["KEY_ERROR"]?.any { message.contains(it, ignoreCase = true) } == true) {
                    onFailure("USSD Failed: $message")
                    _ussdState.value = UssdUiState.Error("USSD Failed: $message")
                } else {
                    onSuccess(message)
                    _ussdState.value = UssdUiState.Success(message)
                }
            }

            override fun over(message: String) {

                if (map["KEY_ERROR"]?.any { message.contains(it, ignoreCase = true) } == true) {
                    onFailure("USSD Session Ended with Error: $message")
                    _ussdState.value = UssdUiState.Error("USSD Session Ended with Error: $message")
                } else {
                    onSuccess(message)
                    _ussdState.value = UssdUiState.Success(message)
                }
            }
        })

    }

    private fun checkAndRequestPermissions(activity: Activity) {
        val permissionsNeeded = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
        )

        val permissionsToRequest = permissionsNeeded.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            checkOverlayPermission(activity)
        }
    }

    private fun checkOverlayPermission(activity: Activity) {
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:${activity.packageName}")
            activity.startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
        }
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1
        const val REQUEST_CODE_OVERLAY_PERMISSION = 2
    }
}
