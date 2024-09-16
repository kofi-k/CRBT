package com.crbt.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.example.crbtjetcompose.core.model.data.CrbtUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val phoneAuthRepository: PhoneAuthRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : ViewModel() {

    val userResultState: StateFlow<Result<CrbtUser>> =
        crbtPreferencesRepository.userPreferencesData
            .map { userPreferences ->
                Result.Success(
                    CrbtUser(
                        userId = userPreferences.userId,
                        phoneNumber = userPreferences.phoneNumber,
                        profileUrl = userPreferences.profileUrl,
                        firstName = userPreferences.firstName,
                        lastName = userPreferences.lastName,
                        accountBalance = 0.0,
                        email = userPreferences.email,
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = Result.Loading,
            )

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> = _signOutState

    fun signOut(signedOut: () -> Unit) {
        _signOutState.value = SignOutState.Loading
        viewModelScope.launch {
            val result = phoneAuthRepository.signOut()
            _signOutState.value = result
            if (result is SignOutState.Success) {
                signedOut()
            }
        }
    }


    fun saveProfileImage(url: String) {
        viewModelScope.launch {
            crbtPreferencesRepository.setUserProfilePictureUrl(url)
        }
    }


    fun saveProfile(
        firstName: String,
        lastName: String,
    ) {
        viewModelScope.launch {
            crbtPreferencesRepository.setUserInfo(
                firstName = firstName,
                lastName = lastName,
                email = "", // todo add email for user
            )
        }
    }

}
