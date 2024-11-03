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
    )

    suspend fun updateCrbtSubscriptionId(subscriptionId: Int)


    suspend fun setUserProfilePictureUrl(profilePictureUrl: String)

    suspend fun clearUserPreferences()

    suspend fun setUserBalance(balance: Double)

    suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean)

    suspend fun setUserCrbtRegistrationStatus(isRegistered: Boolean)
}

