package com.crbt.data.core.data.model

data class OnboardingSetupData(
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    var selectedLanguage: String = "",
)

fun OnboardingSetupData.userProfileIsComplete(): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty()
}