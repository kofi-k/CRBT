package com.crbt.data.core.data.repository.network

import com.example.crbtjetcompose.core.network.model.CrbtNetworkAds
import com.example.crbtjetcompose.core.network.model.CrbtNetworkPackage
import com.example.crbtjetcompose.core.network.model.Login
import com.example.crbtjetcompose.core.network.model.LoginResponse
import com.example.crbtjetcompose.core.network.model.NetworkPackageItem
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.NetworkUserAccountInfo
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import com.example.crbtjetcompose.core.network.model.UpdateUserInfo
import com.example.crbtjetcompose.core.network.retrofit.RetrofitCrbtNetworkApi
import javax.inject.Inject

class CrbtNetworkRepositoryImpl @Inject constructor(
    private val retrofitCrbtNetworkApi: RetrofitCrbtNetworkApi,
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

    override suspend fun getUserAccountInfo(): NetworkUserAccountInfo =
        retrofitCrbtNetworkApi.getUserAccountInfo()

    override suspend fun updateUserAccountInfo(
        firstName: String,
        lastName: String,
    ) {
        retrofitCrbtNetworkApi.updateUserAccountInfo(
            UpdateUserInfo(
                firstName = firstName,
                lastName = lastName,
            )
        )
    }
}