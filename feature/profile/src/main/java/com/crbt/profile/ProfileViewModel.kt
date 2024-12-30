package com.crbt.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UpdateUserInfoUiState
import com.crbt.data.core.data.repository.UserManager
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val phoneAuthRepository: PhoneAuthRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val userManager: UserManager,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
) : ViewModel() {


    var userInfoUiState by mutableStateOf<UpdateUserInfoUiState>(UpdateUserInfoUiState.Idle)
        private set

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> = _signOutState


    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = WhileSubscribed(5_000),
            )


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


    fun saveLanguageCode(languageCode: String) {
        viewModelScope.launch {
            crbtPreferencesRepository.setUserLanguageCode(languageCode)
        }
    }

    fun saveProfile(
        firstName: String,
        lastName: String,
        email: String,
        profile: String,
        onSuccessfulUpdate: () -> Unit,
        onFailedUpdate: (message: String) -> Unit,
    ) {
        userInfoUiState = UpdateUserInfoUiState.Loading
        viewModelScope.launch {
            when (val state = userManager.updateUserInfo(
                firstName = firstName,
                lastName = lastName,
                email = email,
                profile = profile.ifBlank { null }
            )) {
                is UpdateUserInfoUiState.Success -> {
                    crbtPreferencesRepository.updateUserPreferences(
                        crbtPreferencesRepository.userPreferencesData.first().copy(
                            profileUrl = profile
                        )
                    )
                    onSuccessfulUpdate()
                    userInfoUiState = state
                }

                is UpdateUserInfoUiState.Error -> {
                    onFailedUpdate(state.message)
                    userInfoUiState = state
                }

                else -> {
                    userInfoUiState = state
                }
            }
        }
    }
}
