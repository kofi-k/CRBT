package com.crbt.onboarding.ui.phoneAuth


import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.example.crbtjetcompose.core.network.repository.CrbtNetworkRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val repository: PhoneAuthRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val crbtNetworkRepository: CrbtNetworkRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState
    private val _verificationId = MutableStateFlow<String?>(null)


    fun sendVerificationCode(
        phoneNumber: String,
        onOtpSent: (String) -> Unit,
        activity: Activity
    ) {
        _authState.value = AuthState.Loading
        repository.sendVerificationCode(
            phoneNumber = phoneNumber,
            activity = activity,
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("PhoneAuthViewModel", "onVerificationCompleted: $credential")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _authState.value = AuthState.Error(e.message)
                    onOtpSent(e.message ?: "Error")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _authState.value = AuthState.CodeSent(verificationId)
                    _verificationId.value = verificationId
                    onOtpSent("OTP sent successfully")
                }
            })
    }

    fun verifyCode(
        onOtpVerified: () -> Unit,
        otpCode: String
    ) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = repository.verifyCode(_verificationId.value ?: "", otpCode).await()
                val tokenUid = result.user?.uid
                crbtNetworkRepository.login(
                    phone = repository.getSignedInUser()?.phoneNumber ?: "",
                    idToken = tokenUid ?: ""
                ) // todo proceed on success of this call
                _authState.value = AuthState.Success(result)
                crbtPreferencesRepository.setUserId(tokenUid ?: "")
                onOtpVerified()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message)
                crbtPreferencesRepository.setUserId("fakeUserId") // todo remove this, just for testing
            }
        }
    }

}


sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class CodeSent(val verificationId: String) : AuthState()
    data class Success(val result: AuthResult) : AuthState()
    data class Error(val message: String?) : AuthState()
}