package com.crbt.data.core.data

data class OnboardingSetupData(
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    var selectedLanguage: String = CRBTSettingsData.languages.first().id,
)

fun OnboardingSetupData.userProfileIsComplete(): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty()
}