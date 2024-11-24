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

    override val isUserRegisteredForCrbt: Flow<Boolean> =
        crbtPreferencesDataSource.isUserRegisteredForCrbt


    override suspend fun setSignInToken(token: String) =
        crbtPreferencesDataSource.setSignInToken(token)

    override suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        langPref: String,
        rewardPoints: Int
    ) {
        crbtPreferencesDataSource.setUserInfo(
            firstName,
            lastName,
            phoneNumber,
            langPref,
            rewardPoints
        )
        analyticsHelper.logUserDetails(firstName, lastName, phoneNumber, langPref)
    }

    override suspend fun updateCrbtSubscriptionId(subscriptionId: Int) {
        crbtPreferencesDataSource.updateCrbtSubscriptionId(subscriptionId.toString())
        analyticsHelper.logCrbtToneSubscription(subscriptionId.toString())
    }

    override suspend fun setUserProfilePictureUrl(profilePictureUrl: String) =
        crbtPreferencesDataSource.setUserProfilePictureUrl(profilePictureUrl)


    override suspend fun clearUserPreferences() =
        crbtPreferencesDataSource.clearUserPreferences()

    override suspend fun setUserBalance(balance: Double) =
        crbtPreferencesDataSource.setCurrentBalance(balance)


    override suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean) =
        crbtPreferencesDataSource.setUserInterestedCrbtLanguages(code, isInterested)

    override suspend fun setUserCrbtRegistrationStatus(isRegistered: Boolean) {
        crbtPreferencesDataSource.setUserCrbtRegistrationStatus(isRegistered)
    }

}