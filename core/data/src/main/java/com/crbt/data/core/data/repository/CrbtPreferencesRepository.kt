package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow

interface CrbtPreferencesRepository {

    val userPreferencesData: Flow<UserPreferencesData>

    suspend fun setFirstLaunch(isFirstLaunch: Boolean)

    suspend fun setUserId(userId: String)

    suspend fun setUserName(firstName: String, lastName: String, email: String, phoneNumber: String)


    suspend fun setUserProfilePictureUrl(profilePictureUrl: String)

    suspend fun setUserLanguageCode(languageCode: String)

    suspend fun setUserPaymentMethod(paymentMethod: String)
}

