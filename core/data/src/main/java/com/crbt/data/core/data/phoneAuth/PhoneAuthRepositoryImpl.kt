package com.crbt.data.core.data.phoneAuth

import android.app.Activity
import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhoneAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val crbtPreferencesDataSource: CrbtPreferencesDataSource,
) : PhoneAuthRepository {


    override fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyCode(verificationId: String, code: String): Task<AuthResult> {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        return auth.signInWithCredential(credential)
    }

    override suspend fun signOut(): SignOutState {
        return try {
            auth.signOut()
            crbtPreferencesDataSource.setUserId("")
            SignOutState.Success
        } catch (e: Exception) {
            SignOutState.Error(e.message ?: "Error")
        }
    }

    override suspend fun getSignedInUser(): SignedInUser? = auth.currentUser.toSignedInUser()

}


fun FirebaseUser?.toSignedInUser(): SignedInUser? {
    return this?.let {
        SignedInUser(
            userId = it.uid,
            phoneNumber = it.phoneNumber ?: ""
        )
    }
}