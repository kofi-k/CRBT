package com.crbt.data.core.data

data  class OnboardingSetupData(
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    var selectedLanguage: CRBTLanguage = CRBTLanguage.ENGLISH,
)

fun OnboardingSetupData.userProfileIsComplete(): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty()
}