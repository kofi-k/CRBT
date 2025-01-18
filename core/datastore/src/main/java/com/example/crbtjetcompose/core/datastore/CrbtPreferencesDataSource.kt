package com.example.crbtjetcompose.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.itengs.crbt.core.model.data.UserPreferencesData
import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand
import kotlinx.coroutines.flow.first
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
                profileUrl = it.userProfileUrl,
                firstName = it.userFirstName,
                lastName = it.userLastName,
                email = it.userEmail,
                currentBalance = it.userBalance,
                interestedCrbtLanguages = it.interestedCrbtLanguages.keys,
                currentCrbtSubscriptionId = it.currentCrbtSubscriptionId.toIntOrNull() ?: 0,
                giftedCrbtToneIds = it.giftedCrbtToneIds.keys,
                token = it.token,
                rewardPoints = it.rewardPoints,
                numberOfRechargeCodeDigits = if (it.numberOfRechargeCodeDigits == 0) 14 else it.numberOfRechargeCodeDigits,
                autoDialRechargeCode = it.autoDialRechargeCode,
                userLocation = it.userLocation,
                userCrbtRegistrationPackage = it.userCrbtRegistrationPackage,
                themeBrand = when (it.themeBrand) {
                    null,
                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                    ThemeBrandProto.UNRECOGNIZED,
                    ThemeBrandProto.THEME_BRAND_DEFAULT,
                    -> ThemeBrand.DEFAULT

                    ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
                },
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                    ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                useDynamicColor = it.useDynamicColor,
            )
        }

    val isUserRegisteredForCrbt = userPreferences.data.map { it.isUserRegisteredForCrbt }

    val isSystemUnderMaintenance = userPreferences.data.map { it.isSystemUnderMaintenance }


    suspend fun updateUserPreferences(
        userPreferencesData: UserPreferencesData
    ) {
        userPreferences.updateData {
            it.copy {
                userId = userPreferencesData.userId
                userPhoneNumber = userPreferencesData.phoneNumber
                userLanguageCode = userPreferencesData.languageCode
                userProfileUrl = userPreferencesData.profileUrl
                userFirstName = userPreferencesData.firstName
                userLastName = userPreferencesData.lastName
                userEmail = userPreferencesData.email
                userBalance = userPreferencesData.currentBalance
                rewardPoints = userPreferencesData.rewardPoints
                userLocation = userPreferencesData.userLocation
                currentCrbtSubscriptionId = userPreferencesData.currentCrbtSubscriptionId.toString()
            }
        }
    }


    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        userPreferences.updateData {
            it.copy {
                this.themeBrand = when (themeBrand) {
                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
                }
            }
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM ->
                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM

                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
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

    suspend fun setIsSystemUnderMaintenance(isUnderMaintenance: Boolean) {
        userPreferences.updateData {
            it.copy {
                isSystemUnderMaintenance = isUnderMaintenance
            }
        }
    }


    suspend fun setAutoDialRechargeCode(
        autoDial: Boolean,
    ) {
        userPreferences.updateData {
            it.copy {
                autoDialRechargeCode = autoDial
            }
        }
    }

    suspend fun setRequiredRechargeDigits(
        numberOfDigits: Int
    ) {
        userPreferences.updateData {
            it.copy {
                numberOfRechargeCodeDigits = numberOfDigits
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

    suspend fun setUserCrbtRegistrationStatus(isRegistered: Boolean, packageDuration: String) {
        userPreferences.updateData {
            it.copy {
                isUserRegisteredForCrbt = isRegistered
                userCrbtRegistrationPackage = packageDuration
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
        points: Int
    ) {
        try {
            userPreferences.updateData {
                it.copy {
                    userFirstName = firstName
                    userLastName = lastName
                    userPhoneNumber = phone
                    userLanguageCode = languageCode
                    rewardPoints = points
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun saveUserContacts(contacts: String) {
        userPreferences.updateData {
            it.copy {
                userContacts = contacts
            }
        }
    }

    suspend fun getUserContacts(): String =
        userPreferences.data.map { it.userContacts }.first()


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
                    rewardPoints = 0
                    userLocation = ""
                    userContacts = ""
                    isUserRegisteredForCrbt = false
                    autoDialRechargeCode = false
                    numberOfRechargeCodeDigits = 14
                    themeBrand = ThemeBrandProto.THEME_BRAND_DEFAULT
                    userCrbtRegistrationPackage = ""
                }
            }
        } catch (ioException: IOException) {
            Log.e("CrbtPreferences", "Failed to update user preferences", ioException)
        }
    }
}