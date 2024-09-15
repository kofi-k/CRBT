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

    suspend fun signOut(): SignOutState

    suspend fun getSignedInUser(): SignedInUser?

}

sealed interface SignOutState {
    data object Loading : SignOutState
    data object Success : SignOutState
    data class Error(val message: String) : SignOutState
}


data class SignedInUser(
    val userId: String,
    val phoneNumber: String,
)