package com.crbt.data.core.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.UssdResponseCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UssdRepository(
    private val context: Context,
) {
    private val _ussdState = MutableStateFlow<UssdUiState>(UssdUiState.Idle)
    val ussdState: StateFlow<UssdUiState> get() = _ussdState

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (Int) -> Unit) {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
            return
        }

        _ussdState.value = UssdUiState.Loading

        telephonyManager.sendUssdRequest(ussdCode, object : UssdResponseCallback() {
            override fun onReceiveUssdResponse(
                telephonyManager: TelephonyManager,
                request: String,
                response: CharSequence
            ) {
                _ussdState.value = UssdUiState.Success(response.toString())
                onSuccess()
            }

            override fun onReceiveUssdResponseFailed(
                telephonyManager: TelephonyManager,
                request: String,
                failureCode: Int
            ) {
                _ussdState.value = UssdUiState.Error(failureCode)
                onError(failureCode)
            }
        }, Handler(Looper.getMainLooper()))
    }
}


sealed class UssdUiState {
    data object Idle : UssdUiState()
    data object Loading : UssdUiState()
    data class Success(val response: String) : UssdUiState()
    data class Error(val errorCode: Int) : UssdUiState()
}


