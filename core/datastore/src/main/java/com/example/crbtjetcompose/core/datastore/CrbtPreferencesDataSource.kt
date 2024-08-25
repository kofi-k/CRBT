package com.example.crbtjetcompose.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CrbtPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userPreferencesData = userPreferences.data
        .map {
            UserPreferencesData(
                userId = it.userId,
                phoneNumber = it.userPhoneNumber,
                languageCode = it.userLanguageCode,
                paymentMethod = it.userPaymentMethod,
                profileUrl = it.userProfileUrl,
                currency = it.userCurrency,
                firstName = it.userFirstName,
                lastName = it.userLastName
            )
        }

    suspend fun setUserId(id: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    userId = id
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }


    suspend fun setUserLanguageCode(languageCode: String) {
        userPreferences.updateData {
            it.copy {
                userLanguageCode = languageCode
            }
        }
    }

    suspend fun setUserProfilePictureUrl(profilePictureUrl: String) {
        userPreferences.updateData {
            it.copy {
                userProfileUrl = profilePictureUrl
            }
        }
    }

    suspend fun setUserPaymentMethod(paymentMethod: String) {
        userPreferences.updateData {
            it.copy {
                userPaymentMethod = paymentMethod
            }
        }
    }

    suspend fun setUserCurrency(currency: String) {
        userPreferences.updateData {
            it.copy {
                userCurrency = currency
            }
        }
    }

    suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ) {
        try {
            userPreferences.updateData {
                it.copy {
                    userFirstName = firstName
                    userLastName = lastName
                    userEmail = email
                    userPhoneNumber = phoneNumber
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setSubscribedCrbtToneIds(ids: Set<String>) {
        try {
            userPreferences.updateData {
                it.copy {
                    subscribedCrbtToneIds.putAll(ids.associateWith { true })
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setUnsubscribedCrbtToneIds(ids: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    unsubscribedCrbtToneIds.put(ids, true)
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }
}