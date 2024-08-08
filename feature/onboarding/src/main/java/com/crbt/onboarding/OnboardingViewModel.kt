package com.crbt.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.crbt.data.core.data.CRBTLanguage
import com.crbt.data.core.data.OnboardingScreenData
import com.crbt.data.core.data.OnboardingSetupData
import com.crbt.data.core.data.OnboardingSetupProcess
import com.crbt.data.core.data.userProfileIsComplete
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

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

    private var onboardingIndex = 0

    private var _isNextEnabled by mutableStateOf(false)
    val isNextEnabled
        get() = _isNextEnabled

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

    fun onLanguageSelected(language: CRBTLanguage) {
        _onboardingSetupData = _onboardingSetupData.copy(selectedLanguage = language)
        _isNextEnabled = getIsNextEnabled()
    }

    fun onPhoneNumberEntered(phoneNumber: String, isPhoneNumberValid: Boolean) {
        _onboardingSetupData = _onboardingSetupData.copy(phoneNumber = phoneNumber)
        _isNextEnabled = true
    }

    fun onUserProfileEntered(firstName: String, lastName: String) {
        _onboardingSetupData = _onboardingSetupData.copy(firstName = firstName, lastName = lastName)
        _isNextEnabled = getIsNextEnabled()
    }



    private fun changeOnboardingSetupData(index: Int) {
        onboardingIndex = index
        _onboardingScreenData = createOnboardingScreenData()
        _isNextEnabled = getIsNextEnabled()
    }

    private fun getIsNextEnabled() : Boolean {
        return when (onboardingOrder[onboardingIndex]) {
            OnboardingSetupProcess.LANGUAGE_SELECTION -> true
            OnboardingSetupProcess.PHONE_NUMBER_ENTRY -> _onboardingSetupData.phoneNumber.isNotEmpty() // todo use libphonenumber to validate
            OnboardingSetupProcess.OTP_VERIFICATION -> true // todo validate otp
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