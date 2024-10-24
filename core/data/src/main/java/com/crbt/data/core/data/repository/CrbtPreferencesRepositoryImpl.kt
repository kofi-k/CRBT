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

    override suspend fun setSignInToken(token: String) =
        crbtPreferencesDataSource.setSignInToken(token)

    override suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        langPref: String
    ) {
        crbtPreferencesDataSource.setUserInfo(
            firstName,
            lastName,
            phoneNumber,
            langPref
        )
        analyticsHelper.logUserDetails(firstName, lastName, phoneNumber, langPref)
    }

    override suspend fun setPhoneNumber(phoneNumber: String) =
        crbtPreferencesDataSource.setPhoneNumber(phoneNumber)

    override suspend fun setUserProfilePictureUrl(profilePictureUrl: String) =
        crbtPreferencesDataSource.setUserProfilePictureUrl(profilePictureUrl)


    override suspend fun setUserPaymentMethod(paymentMethod: String) {
        crbtPreferencesDataSource.setUserPaymentMethod(paymentMethod)
        analyticsHelper.logUserPreferedCurrency(paymentMethod)
    }

    override suspend fun clearUserPreferences() =
        crbtPreferencesDataSource.clearUserPreferences()

    override suspend fun setUserBalance(balance: Double) =
        crbtPreferencesDataSource.setCurrentBalance(balance)


    override suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean) =
        crbtPreferencesDataSource.setUserInterestedCrbtLanguages(code, isInterested)

}