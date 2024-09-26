package com.crbt.data.core.data.phoneAuth

import android.app.Activity
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
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
    private val crbtPreferencesRepository: CrbtPreferencesRepository
) : PhoneAuthRepository {


    override fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: Activity
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyCode(verificationId: String, code: String): Task<AuthResult> {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        return auth.signInWithCredential(credential)
    }

    override suspend fun signOut(): SignOutState = try {
        SignOutState.Loading
        auth.signOut()
        crbtPreferencesRepository.clearUserPreferences()
        SignOutState.Success
    } catch (e: Exception) {
        SignOutState.Error(e.message ?: "Error")
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