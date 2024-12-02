package com.crbt.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.LoginManager
import com.crbt.data.core.data.repository.UpdateUserInfoUiState
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val phoneAuthRepository: PhoneAuthRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val loginManager: LoginManager,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
) : ViewModel() {

    private val _userInfoUiState =
        MutableStateFlow<UpdateUserInfoUiState>(UpdateUserInfoUiState.Idle)
    val userInfoUiState: StateFlow<UpdateUserInfoUiState> = _userInfoUiState.asStateFlow()

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
            loginManager.updateUserInfo(
                firstName = firstName,
                lastName = lastName
            ).collect {
                _userInfoUiState.value = it
            }
        }
    }
}
