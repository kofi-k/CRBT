package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.analytics.AnalyticsHelper
import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CrbtPreferencesRepositoryImpl @Inject constructor(
    private val crbtPreferencesDataSource: CrbtPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : CrbtPreferencesRepository {

    override val userPreferencesData: Flow<UserPreferencesData> =
        crbtPreferencesDataSource.userPreferencesData

    override suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserId(userId: String) =
        crbtPreferencesDataSource.setUserId(userId)

    override suspend fun setUserName(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ) =
        crbtPreferencesDataSource.setUserInfo(firstName, lastName, email, phoneNumber)

    override suspend fun setUserProfilePictureUrl(profilePictureUrl: String) =
        crbtPreferencesDataSource.setUserProfilePictureUrl(profilePictureUrl)

    override suspend fun setUserLanguageCode(languageCode: String) {
        crbtPreferencesDataSource.setUserLanguageCode(languageCode)
        analyticsHelper.logUserPreferedLanguage(languageCode)
    }

    override suspend fun setUserPaymentMethod(paymentMethod: String) {
        crbtPreferencesDataSource.setUserPaymentMethod(paymentMethod)
        analyticsHelper.logUserPreferedCurrency(paymentMethod)
    }
}