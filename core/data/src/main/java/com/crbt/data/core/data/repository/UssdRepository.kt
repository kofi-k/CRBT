package com.crbt.data.core.data.repository

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
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

        checkAccessibilityPermission(activity)

        _ussdState.value = UssdUiState.Loading

        val map = HashMap<String, HashSet<String>>().apply {
            put("KEY_LOGIN", HashSet(listOf("espere", "waiting", "loading", "esperando")))
            put(
                "KEY_ERROR",
                HashSet(listOf("problema", "problem", "error", "null", "failed", "insufficient"))
            )
        }

        ussdController.callUSSDOverlayInvoke(
            ussdCode, map,
            object : USSDController.CallbackInvoke {
                override fun responseInvoke(message: String) {
                    if (map["KEY_ERROR"]?.any { message.contains(it, ignoreCase = true) } == true) {
                        onFailure("USSD Failed: $message")
                        _ussdState.value = UssdUiState.Error("USSD invoke Failed: $message")
                    } else {
                        onSuccess(message)
                        _ussdState.value = UssdUiState.Success(message)
                    }
                }

                override fun over(message: String) {
                    if (map["KEY_ERROR"]?.any { message.contains(it, ignoreCase = true) } == true) {
                        onFailure("USSD Session Ended with Error: $message")
                        _ussdState.value =
                            UssdUiState.Error("USSD Session Ended with Error: $message")
                    } else {
                        val accessibilityError = "Check your accessibility | overlay permission"
                        if (message.contains(accessibilityError, ignoreCase = true)) {
                            onFailure(accessibilityError)
                            _ussdState.value = UssdUiState.Error(accessibilityError)
                        } else {
                            onSuccess(message)
                            _ussdState.value = UssdUiState.Success(message)
                        }
                    }
                }
            }
        )
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

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val service = "${context.packageName}/com.romellfudi.ussdlibrary.USSDService"
        val enabledServicesSetting = Settings.Secure.getString(
            context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val accessibilityEnabled = Settings.Secure.getInt(
            context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0
        )
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals(service, ignoreCase = true)) {
                return accessibilityEnabled == 1
            }
        }
        return false
    }

    private fun checkAccessibilityPermission(activity: Activity) {
        if (!isAccessibilityServiceEnabled(context)) {
            AlertDialog.Builder(activity)
                .setTitle("Enable Accessibility Service")
                .setMessage("You must enable the accessibility service to allow USSD operations.")
                .setPositiveButton("OK") { _, _ ->
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    activity.startActivity(intent)
                }
                .show()
        }
    }


    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1
        const val REQUEST_CODE_OVERLAY_PERMISSION = 2
    }
}
