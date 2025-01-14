package com.itengs.crbt.core.model.data

import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand

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
    val rewardPoints: Int,
    val autoDialRechargeCode: Boolean,
    val numberOfRechargeCodeDigits: Int,
    val userLocation: String,
    val userCrbtRegistrationPackage: String,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
)


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
