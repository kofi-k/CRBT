package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.analytics.AnalyticsHelper
import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override suspend fun updateUserPreferences(userPreferencesData: UserPreferencesData) {
        crbtPreferencesDataSource.updateUserPreferences(userPreferencesData)
        analyticsHelper.logUserDetails(
            userPreferencesData.firstName,
            userPreferencesData.lastName,
            userPreferencesData.phoneNumber,
            userPreferencesData.languageCode
        )
    }

    override suspend fun setUserLanguageCode(languageCode: String) =
        crbtPreferencesDataSource.updateUserPreferences(
            userPreferenceData().copy(languageCode = languageCode)
        )

    override suspend fun updateCrbtSubscriptionId(subscriptionId: Int) {
        crbtPreferencesDataSource.updateUserPreferences(
            userPreferenceData().copy(currentCrbtSubscriptionId = subscriptionId)
        )
        analyticsHelper.logCrbtToneSubscription(subscriptionId.toString())
    }

    override suspend fun updateUserLocation(location: String) =
        crbtPreferencesDataSource.updateUserPreferences(
            userPreferenceData().copy(userLocation = location)
        )

    override suspend fun setUserProfilePictureUrl(profilePictureUrl: String) =
        crbtPreferencesDataSource.setUserProfilePictureUrl(profilePictureUrl)


    override suspend fun clearUserPreferences() =
        crbtPreferencesDataSource.clearUserPreferences()

    override suspend fun setUserBalance(balance: Double) =
        crbtPreferencesDataSource.setCurrentBalance(balance)


    override suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean) =
        crbtPreferencesDataSource.setUserInterestedCrbtLanguages(code, isInterested)

    override suspend fun setUserCrbtRegistrationStatus(isRegistered: Boolean) =
        crbtPreferencesDataSource.setUserCrbtRegistrationStatus(isRegistered)


    override suspend fun setAutoDialRechargeCode(
        autoDial: Boolean,
    ) = crbtPreferencesDataSource.setAutoDialRechargeCode(autoDial)


    override suspend fun setRequiredRechargeDigits(
        numberOfDigits: Int
    ) = crbtPreferencesDataSource.setRequiredRechargeDigits(numberOfDigits)


    override suspend fun saveUserContacts(contacts: List<String>) =
        crbtPreferencesDataSource.saveUserContacts(contacts.joinToString(","))


    override suspend fun getUserContacts(): String = crbtPreferencesDataSource.getUserContacts()

    private suspend fun userPreferenceData() = userPreferencesData.first()
}