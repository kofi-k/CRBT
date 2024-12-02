package com.example.crbtjetcompose.core.model.data

data class UserPreferencesData(
    val userId: String,
    val token: String,
    val isUserSignedIn: Boolean,
    val phoneNumber: String,
    val languageCode: String,
    val profileUrl: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val currentBalance: Double,
    val interestedCrbtLanguages: Set<String>,
    val currentCrbtSubscriptionId: Int,
    val giftedCrbtToneIds: Set<String>,
    val rewardPoints: Int
)


fun UserPreferencesData.isProfileSetupComplete(): Boolean =
    firstName.isNotBlank() && lastName.isNotBlank()

fun UserPreferencesData.fullName(): String {
    return "$firstName $lastName"
}

fun UserPreferencesData.asCrbtUser() = CrbtUser(
    userId = userId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    email = email,
    accountBalance = currentBalance,
    profileUrl = profileUrl,
)
