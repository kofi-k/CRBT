package com.example.crbtjetcompose.core.datastore

import androidx.datastore.core.DataStore
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CrbtPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userPreferencesData: Flow<UserPreferencesData> = userPreferences.data
        .map {
            UserPreferencesData(
                userId = it.userId,
                phoneNumber = it.userPhoneNumber,
                languageCode = it.userLanguageCode,
                paymentMethod = it.userPaymentMethod,
                profileUrl = it.userProfileUrl,
                currency = it.userCurrency
            )
        }

}