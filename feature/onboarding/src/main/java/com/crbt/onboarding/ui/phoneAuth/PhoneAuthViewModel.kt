package com.crbt.onboarding.ui.phoneAuth


import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.repository.UserManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.mockito.Mockito.mock
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val repository: PhoneAuthRepository,
    private val userManager: UserManager,
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    private val _verificationId = MutableStateFlow<String?>(null)


    fun sendVerificationCode(
        phoneNumber: String,
        onOtpSent: (String) -> Unit,
        onFailed: (String) -> Unit,
        activity: Activity
    ) {
        authState = AuthState.Loading
        repository.sendVerificationCode(
            phoneNumber = phoneNumber,
            activity = activity,
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    authState = AuthState.Success(mock(AuthResult::class.java))
                }

                override fun onVerificationFailed(e: FirebaseException) {
//                    _authState.value = AuthState.Error(e.message)
                    authState = AuthState.CodeSent("FAKE_ID") // todo remove these
                    _verificationId.value = "FAKE_ID"
                    onOtpSent("Your OTP is 123456")
                    onFailed(e.message ?: "Verification failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    authState = AuthState.CodeSent(verificationId)
                    _verificationId.value = verificationId
                    onOtpSent("OTP sent successfully")
                }
            })
    }

    fun verifyCode(
        otpCode: String,
        phone: String,
        accountType: String,
        langPref: String,
        onFailed: (String) -> Unit,
        onVerified: () -> Unit,
    ) {
        authState = AuthState.Loading
        viewModelScope.launch {
            try {
//                val result = repository.verifyCode(_verificationId.value ?: "", otpCode).await()
//                val fakeAuthResult = mock(AuthResult::class.java)
//                val tokenUid = result.user?.uid
                userManager.login(phone, accountType, langPref)
                onVerified()
//                AuthState.Success(fakeAuthResult)
            } catch (e: IOException) {
                onFailed("A network error occurred. Please check your connection and try again.")
                authState = when (e) {
                    is ConnectException -> AuthState.Error("Oops! your internet connection seem to be off.")
                    is SocketTimeoutException -> AuthState.Error("Hmm, connection timed out")
                    is UnknownHostException -> AuthState.Error("A network error occurred. Please check your connection and try again.")
                    else -> AuthState.Error(e.message ?: "An error occurred")
                }
            } catch (e: Exception) {
                onFailed(e.message ?: "Verification failed")
                authState = AuthState.Error(e.message)
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