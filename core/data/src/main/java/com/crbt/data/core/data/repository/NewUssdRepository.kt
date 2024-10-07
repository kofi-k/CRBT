package com.crbt.data.core.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.romellfudi.ussdlibrary.USSDController
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NewUssdRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ussdController: USSDController
) {

    private val _ussdState = MutableStateFlow<UssdUiState>(UssdUiState.Idle)
    val ussdState: StateFlow<UssdUiState> get() = _ussdState

    fun dialUssdCode(ussdCode: String, callback: (String) -> Unit) {
        _ussdState.value = UssdUiState.Loading

        checkAndRequestPermissions()

        val map = HashMap<String, HashSet<String>>().apply {
            put("KEY_LOGIN", HashSet(listOf("espere", "waiting", "loading", "esperando")))
            put("KEY_ERROR", HashSet(listOf("problema", "problem", "error", "null")))
        }

        ussdController.callUSSDOverlayInvoke(ussdCode, map, object : USSDController.CallbackInvoke {
            override fun responseInvoke(message: String) {
                callback(message)
                _ussdState.value = UssdUiState.Success(message)
            }

            override fun over(message: String) {
                callback("USSD session ended: $message")
                _ussdState.value = UssdUiState.Error("USSD session ended: $message")
            }
        })
    }

    private fun checkAndRequestPermissions() {
        val permissionsNeeded = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
        )

        val permissionsToRequest = permissionsNeeded.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            checkOverlayPermission()
        }
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$this")
            startActivity(
                context,
                intent,
                null
            )
        }
    }


    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1
        const val REQUEST_CODE_OVERLAY_PERMISSION = 2
    }
}