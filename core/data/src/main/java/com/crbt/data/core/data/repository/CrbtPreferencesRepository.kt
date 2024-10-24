package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow

interface CrbtPreferencesRepository {

    val userPreferencesData: Flow<UserPreferencesData>

    suspend fun setFirstLaunch(isFirstLaunch: Boolean)

    suspend fun setSignInToken(token: String)

    suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        langPref: String,
    )

    suspend fun setPhoneNumber(phoneNumber: String)

    suspend fun setUserProfilePictureUrl(profilePictureUrl: String)

    suspend fun setUserPaymentMethod(paymentMethod: String)

    suspend fun clearUserPreferences()

    suspend fun setUserBalance(balance: Double)

    suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean)

}

