package com.crbt.onboarding.ui.phoneAuth


import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.repository.UserManager
import com.example.crbtjetcompose.core.network.di.HttpException
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
    private val userManager: UserManager,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val phoneAuthState: StateFlow<AuthState> get() = _authState
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
        otpCode: String,
        phone: String,
        accountType: String,
        langPref: String
    ) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value = try {
                val result = repository.verifyCode(_verificationId.value ?: "", otpCode).await()
                val tokenUid = result.user?.uid
                userManager.login(phone, accountType, langPref)
                AuthState.Success(result)
            } catch (e: Exception) {
                _authState.value = AuthState.Loading
                userManager.login(
                    phone,
                    accountType,
                    langPref
                ) // todo remove this line, it's for testing
                AuthState.Error(e.message)
            } catch (e: HttpException) {
                AuthState.Error(e.message)
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