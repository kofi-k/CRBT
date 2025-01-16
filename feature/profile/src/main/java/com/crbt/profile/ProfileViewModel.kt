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
import com.crbt.domain.DestroyMediaControllerUseCase
import com.itengs.crbt.core.model.data.UserPreferencesData
import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val phoneAuthRepository: PhoneAuthRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val userManager: UserManager,
    private val destroyMediaControllerUseCase: DestroyMediaControllerUseCase,
) : ViewModel() {


    var userInfoUiState by mutableStateOf<UpdateUserInfoUiState>(UpdateUserInfoUiState.Idle)
        private set


    val userPreferenceUiState: StateFlow<SettingsUiState> =
        crbtPreferencesRepository.userPreferencesData
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        brand = userData.themeBrand,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                    userPreferencesData = userData
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingsUiState.Loading,
            )


    suspend fun signOut(): SignOutState {
        destroyMediaControllerUseCase()
        return phoneAuthRepository.signOut()
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

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            crbtPreferencesRepository.setThemeBrand(themeBrand)
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            crbtPreferencesRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            crbtPreferencesRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
}


/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val settings: UserEditableSettings,
        val userPreferencesData: UserPreferencesData
    ) : SettingsUiState
}