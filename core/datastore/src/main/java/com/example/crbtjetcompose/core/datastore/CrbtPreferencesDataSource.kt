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
                isUserSignedIn = it.isUserSignedIn,
                phoneNumber = it.userPhoneNumber,
                languageCode = it.userLanguageCode,
                paymentMethod = it.userPaymentMethod,
                profileUrl = it.userProfileUrl,
                currency = it.userCurrency,
                firstName = it.userFirstName,
                lastName = it.userLastName,
                email = it.userEmail,
                currentBalance = it.userBalance,
                interestedCrbtLanguages = it.interestedCrbtLanguages.keys,
                currentCrbtSubscriptionId = it.currentCrbtSubscriptionId.toIntOrNull() ?: 0,
                giftedCrbtToneIds = it.giftedCrbtToneIds.keys,
                token = it.token
            )
        }

    suspend fun setSignInToken(userToken: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    token = userToken
                    isUserSignedIn = userToken.isNotBlank()
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
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


    suspend fun setPhoneNumber(phoneNumber: String) {
        userPreferences.updateData {
            it.copy {
                userPhoneNumber = phoneNumber
            }
        }
    }

    suspend fun setCurrentBalance(balance: Double) {
        userPreferences.updateData {
            it.copy {
                userBalance = balance
            }
        }
    }

    suspend fun setUserInterestedCrbtLanguages(code: String, isInterested: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    if (isInterested) {
                        interestedCrbtLanguages.put(code, true)
                    } else {
                        interestedCrbtLanguages.remove(code)
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }


    suspend fun setUserInfo(
        firstName: String,
        lastName: String,
        phone: String,
        languageCode: String,
    ) {
        try {
            userPreferences.updateData {
                it.copy {
                    userFirstName = firstName
                    userLastName = lastName
                    userPhoneNumber = phone
                    userLanguageCode = languageCode
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun updateCrbtSubscriptionId(subscriptionId: String) {
        userPreferences.updateData {
            it.copy {
                currentCrbtSubscriptionId = subscriptionId
            }
        }
    }


    suspend fun clearUserPreferences() {
        try {
            userPreferences.updateData {
                it.copy {
                    userId = ""
                    userPhoneNumber = ""
                    userLanguageCode = ""
                    userPaymentMethod = ""
                    userProfileUrl = ""
                    userCurrency = ""
                    userFirstName = ""
                    userLastName = ""
                    userEmail = ""
                    subscribedCrbtToneIds.clear()
                    unsubscribedCrbtToneIds.clear()
                    giftedCrbtToneIds.clear()
                    interestedCrbtLanguages.clear()
                    currentCrbtSubscriptionId = ""
                    userBalance = 0.0
                    token = ""
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }
}