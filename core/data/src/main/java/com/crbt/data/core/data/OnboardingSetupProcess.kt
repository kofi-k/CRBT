package com.crbt.data.core.data

enum class OnboardingSetupProcess {
    LANGUAGE_SELECTION,
    PHONE_NUMBER_ENTRY,
    OTP_VERIFICATION,
    USER_PROFILE_SETUP,
}

fun OnboardingSetupData.userProfileIsComplete(): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty()
}