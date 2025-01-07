package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow

interface CrbtPreferencesRepository {

    val userPreferencesData: Flow<UserPreferencesData>

    val isUserRegisteredForCrbt: Flow<Boolean>


    suspend fun setSignInToken(token: String)

    suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        langPref: String,
        rewardPoints: Int
    )

    suspend fun updateUserPreferences(userPreferencesData: UserPreferencesData)

    suspend fun setUserLanguageCode(languageCode: String)

    suspend fun updateCrbtSubscriptionId(subscriptionId: Int)

    suspend fun updateUserLocation(location: String)

    suspend fun setUserProfilePictureUrl(profilePictureUrl: String)

    suspend fun clearUserPreferences()

    suspend fun setUserBalance(balance: Double)

    suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean)

    suspend fun setUserCrbtRegistrationStatus(isRegistered: Boolean, packageDuration: String)

    suspend fun setAutoDialRechargeCode(
        autoDial: Boolean,
    )

    suspend fun setRequiredRechargeDigits(
        numberOfDigits: Int
    )

    suspend fun saveUserContacts(contacts: List<String>)

    suspend fun getUserContacts(): String

}

