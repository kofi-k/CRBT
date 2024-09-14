package com.crbt.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.model.DummyUser
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.example.crbtjetcompose.core.model.data.CrbtUser
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val userData: StateFlow<CrbtUser> =
        crbtPreferencesRepository.userPreferencesData
            .map { userPreferences ->
                CrbtUser(
                    userId = userPreferences.userId,
                    phoneNumber = userPreferences.phoneNumber,
                    profileUrl = userPreferences.profileUrl,
                    firstName = userPreferences.firstName,
                    lastName = userPreferences.lastName,
                    accountBalance = 0.0,
                    email = userPreferences.email,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = DummyUser.user,
            )

    private var firstName by mutableStateOf("")
    private var lastName by mutableStateOf("")

    private

    suspend fun signOut(): SignOutState {
        return phoneAuthRepository.signOut()
    }

    fun onNameChanged(firstName: String, lastName: String) {
        this.firstName = firstName
        this.lastName = lastName
    }

    fun saveProfileImage(url: String) {
        viewModelScope.launch {
            crbtPreferencesRepository.setUserProfilePictureUrl(url)
        }
    }


    fun saveProfile() {
        viewModelScope.launch {
            crbtPreferencesRepository.setUserInfo(
                firstName = firstName,
                lastName = lastName,
                email = userData.value.email,
            )
        }
    }

}