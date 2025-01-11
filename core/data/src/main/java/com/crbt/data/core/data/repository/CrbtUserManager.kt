package com.crbt.data.core.data.repository

import com.crbt.core.network.di.HttpException
import com.crbt.core.network.model.UserAccountDetailsNetworkModel
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.itengs.crbt.core.model.data.fullName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CrbtUserManager @Inject constructor(
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val crbtNetworkRepository: CrbtNetworkRepository,
    private val userMetaInfoCollectionRepo: UserMetaInfoCollectionRepo
) : UserManager {
    override val isLoggedIn: Flow<Boolean>
        get() = crbtPreferencesRepository.userPreferencesData.map { it.token.isNotBlank() }

    override suspend fun login(
        phone: String,
        accountType: String,
        langPref: String
    ): String {
        val response = crbtNetworkRepository.login(
            phone = phone,
            accountType = accountType,
            langPref = langPref
        )
        with(response) {
            if (account.firstName.isNullOrBlank() && account.lastName.isNullOrBlank()) {
                crbtPreferencesRepository.clearUserPreferences()
            }
            crbtPreferencesRepository.setSignInToken(token)
            updateUserPreference(account)
        }
        return userPreferenceData().fullName()
    }


    override suspend fun getAccountInfo() {
        val response = crbtNetworkRepository.getUserAccountInfo()
        updateUserPreference(response)
    }

    override suspend fun updateUserInfo(
        firstName: String,
        lastName: String,
        profile: String?,
        email: String?
    ): UpdateUserInfoUiState =
        try {
            val response = crbtNetworkRepository.updateUserAccountInfo(
                firstName = firstName,
                lastName = lastName,
                profile = profile,
                email = email,
                location = userMetaInfoCollectionRepo.getLastKnownLocation()
            )
            userMetaInfoCollectionRepo.uploadUserContacts()
            updateUserPreference(response.updatedAccount.copy(profile = profile ?: ""))
            UpdateUserInfoUiState.Success
        } catch (e: IOException) {
            when (e) {
                is ConnectException -> UpdateUserInfoUiState.Error("Oops! your internet connection seem to be off.")
                is SocketTimeoutException -> UpdateUserInfoUiState.Error("Hmm, connection timed out")
                is UnknownHostException -> UpdateUserInfoUiState.Error("A network error occurred. Please check your connection and try again.")
                else -> UpdateUserInfoUiState.Error(e.message ?: "An error occurred")
            }
        } catch (e: Exception) {
            UpdateUserInfoUiState.Error(e.message ?: "Error")
        } catch (e: HttpException) {
            UpdateUserInfoUiState.Error(e.message ?: "Error")
        }

    private suspend fun updateUserPreference(data: UserAccountDetailsNetworkModel) {
        val userPreferencesData = userPreferenceData().copy(
            firstName = data.firstName ?: "",
            lastName = data.lastName ?: "",
            phoneNumber = data.phone,
            languageCode = data.langPref,
            rewardPoints = data.rewardPoints ?: 0,
            profileUrl = data.profile ?: "",
            userLocation = data.location ?: "",
            currentCrbtSubscriptionId = data.subSongId,
            email = data.email ?: ""
        )
        crbtPreferencesRepository.updateUserPreferences(userPreferencesData)

    }


    private suspend fun userPreferenceData() = crbtPreferencesRepository.userPreferencesData.first()
}

