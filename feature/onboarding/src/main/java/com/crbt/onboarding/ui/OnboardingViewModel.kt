package com.crbt.onboarding.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.OnboardingSetupProcess
import com.crbt.data.core.data.model.OnboardingScreenData
import com.crbt.data.core.data.model.OnboardingSetupData
import com.crbt.data.core.data.model.userProfileIsComplete
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UpdateUserInfoUiState
import com.crbt.data.core.data.repository.UserManager
import com.crbt.ui.core.ui.otp.OTP_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userManager: UserManager,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : ViewModel() {

    private val onboardingOrder: List<OnboardingSetupProcess> = listOf(
        OnboardingSetupProcess.LANGUAGE_SELECTION,
        OnboardingSetupProcess.PHONE_NUMBER_ENTRY,
        OnboardingSetupProcess.OTP_VERIFICATION,
        OnboardingSetupProcess.USER_PROFILE_SETUP,
    )

    private var _onboardingSetupData by mutableStateOf(OnboardingSetupData())
    val onboardingSetupData: OnboardingSetupData
        get() = _onboardingSetupData

    private var _onboardingScreenData by mutableStateOf(createOnboardingScreenData())
    val onboardingScreenData: OnboardingScreenData
        get() = _onboardingScreenData

    private var _otpCode by mutableStateOf("")
    val otpCode: String
        get() = _otpCode

    private var onboardingIndex = 0

    private var _isNextEnabled by mutableStateOf(false)
    val isNextEnabled
        get() = _isNextEnabled

    var userInfoUiState by mutableStateOf<UpdateUserInfoUiState>(UpdateUserInfoUiState.Idle)
        private set


    fun onNextClicked() {
        if (onboardingIndex < onboardingOrder.size - 1) {
            changeOnboardingSetupData(onboardingIndex + 1)
            _isNextEnabled = false
        }
    }

    fun onPreviousClicked() {
        if (onboardingIndex > 0) {
            onboardingIndex--
            changeOnboardingSetupData(onboardingIndex)
        }
    }

    fun onDoneClicked() {
        _onboardingScreenData = createOnboardingScreenData()
    }

    fun updateUserProfileInfo(
        onSuccessfulUpdate: () -> Unit,
        onFailedUpdate: (message: String) -> Unit,
        email: String
    ) {
        userInfoUiState = UpdateUserInfoUiState.Loading
        viewModelScope.launch {
            when (val state = userManager.updateUserInfo(
                firstName = _onboardingSetupData.firstName,
                lastName = _onboardingSetupData.lastName,
                profile = "",
                email = email
            )) {
                is UpdateUserInfoUiState.Success -> {
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


    fun onLanguageSelected(code: String) {
        viewModelScope.launch {
            _onboardingSetupData = _onboardingSetupData.copy(selectedLanguage = code)
            crbtPreferencesRepository.setUserLanguageCode(code)
            _isNextEnabled = getIsNextEnabled()
        }
    }

    fun onPhoneNumberEntered(phoneNumber: String, isPhoneNumberValid: Boolean) {
        _onboardingSetupData = _onboardingSetupData.copy(phoneNumber = phoneNumber)
        _isNextEnabled = isPhoneNumberValid
    }

    fun onUserProfileEntered(firstName: String, lastName: String, isValid: Boolean) {
        _onboardingSetupData = _onboardingSetupData.copy(firstName = firstName, lastName = lastName)
        _isNextEnabled = isValid
    }

    fun onOtpCodeChanged(otp: String, isComplete: Boolean) {
        _otpCode = otp
        _isNextEnabled = getIsNextEnabled()
    }


    private fun changeOnboardingSetupData(index: Int) {
        onboardingIndex = index
        _onboardingScreenData = createOnboardingScreenData()
        _isNextEnabled = getIsNextEnabled()
    }

    private fun getIsNextEnabled(): Boolean {
        return when (onboardingOrder[onboardingIndex]) {
            OnboardingSetupProcess.LANGUAGE_SELECTION -> true
            OnboardingSetupProcess.PHONE_NUMBER_ENTRY -> _onboardingSetupData.phoneNumber.isNotEmpty() // todo use libphonenumber to validate
            OnboardingSetupProcess.OTP_VERIFICATION -> _otpCode.length == OTP_LENGTH
            OnboardingSetupProcess.USER_PROFILE_SETUP -> _onboardingSetupData.userProfileIsComplete()
        }
    }


    private fun createOnboardingScreenData(): OnboardingScreenData {
        return OnboardingScreenData(
            onboardingScreenIndex = onboardingIndex,
            totalScreenCount = onboardingOrder.size,
            onboardingSetupProcess = onboardingOrder[onboardingIndex],
            shouldShowNextButton = onboardingIndex < onboardingOrder.size - 1,
            shouldShowDoneButton = onboardingIndex == onboardingOrder.size - 1,
        )
    }
}