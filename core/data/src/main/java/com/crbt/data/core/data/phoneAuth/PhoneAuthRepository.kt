package com.crbt.data.core.data.phoneAuth

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthProvider


interface PhoneAuthRepository {
    fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    )

    fun verifyCode(verificationId: String, code: String): Task<AuthResult>

    fun signOut()
}


