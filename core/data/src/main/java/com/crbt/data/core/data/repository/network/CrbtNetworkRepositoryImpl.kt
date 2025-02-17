package com.crbt.data.core.data.repository.network

import com.crbt.core.network.model.AccountUpdatedResponse
import com.crbt.core.network.model.CrbtNetworkAds
import com.crbt.core.network.model.CrbtNetworkPackage
import com.crbt.core.network.model.Login
import com.crbt.core.network.model.LoginResponse
import com.crbt.core.network.model.NetworkPackageItem
import com.crbt.core.network.model.NetworkSongsResource
import com.crbt.core.network.model.SubscriptionRequest
import com.crbt.core.network.model.UpdateUserInfo
import com.crbt.core.network.model.UserAccountDetailsNetworkModel
import com.crbt.core.network.model.UserContacts
import com.crbt.core.network.retrofit.RetrofitCrbtNetworkApi
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CrbtNetworkRepositoryImpl @Inject constructor(
    private val retrofitCrbtNetworkApi: RetrofitCrbtNetworkApi,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : CrbtNetworkRepository {
    override suspend fun getSongs(): List<NetworkSongsResource> {
        return retrofitCrbtNetworkApi.getSongs().allSongs
    }

    override suspend fun getAds(): List<CrbtNetworkAds> {
        return retrofitCrbtNetworkApi.getAds()
    }

    override suspend fun getPackageItems(): List<NetworkPackageItem> {
        return retrofitCrbtNetworkApi.getPackageItems()
    }

    override suspend fun getPackageCategories(): List<CrbtNetworkPackage> {
        return retrofitCrbtNetworkApi.getPackageCategories()
    }

    override suspend fun subscribeToCrbt(songId: Int): String =
        retrofitCrbtNetworkApi.subscribeToCrbt(songId).message


    override suspend fun unsubscribe(subscriptionRequest: SubscriptionRequest) {
        retrofitCrbtNetworkApi.unsubscribe(subscriptionRequest)
    }

    override suspend fun login(
        phone: String,
        accountType: String,
        langPref: String
    ): LoginResponse =
        retrofitCrbtNetworkApi.login(
            Login(
                phone = phone,
                accountType = accountType,
                langPref = langPref
            )
        )

    override suspend fun getUserAccountInfo(): UserAccountDetailsNetworkModel =
        retrofitCrbtNetworkApi.getUserAccountInfo()

    override suspend fun updateUserAccountInfo(
        firstName: String,
        lastName: String,
        profile: String?,
        email: String?,
        location: String
    ): AccountUpdatedResponse = retrofitCrbtNetworkApi.updateUserAccountInfo(
        UpdateUserInfo(
            firstName = firstName,
            lastName = lastName,
            langPref = userPreferenceData().languageCode,
            profile = profile,
            location = location,
            email = email
        )
    )

    override suspend fun uploadUserContacts(contacts: List<String>) =
        retrofitCrbtNetworkApi.uploadUserContacts(UserContacts(contacts))

    private suspend fun userPreferenceData() = crbtPreferencesRepository.userPreferencesData.first()
}