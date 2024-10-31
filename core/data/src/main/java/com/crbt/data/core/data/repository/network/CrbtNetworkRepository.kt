package com.crbt.data.core.data.repository.network

import com.example.crbtjetcompose.core.network.model.CrbtNetworkPackage
import com.example.crbtjetcompose.core.network.model.LoginResponse
import com.example.crbtjetcompose.core.network.model.NetworkPackageItem
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.NetworkUserAccountInfo
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest

interface CrbtNetworkRepository {
    suspend fun getSongs(): List<NetworkSongsResource>

    suspend fun getPackageItems(): List<NetworkPackageItem>

    suspend fun getPackageCategories(): List<CrbtNetworkPackage>

    suspend fun subscribeToCrbt(
        songId: Int
    ): String

    suspend fun unsubscribe(
        subscriptionRequest: SubscriptionRequest
    )

    suspend fun login(
        phone: String,
        accountType: String,
        langPref: String
    ): LoginResponse

    suspend fun getUserAccountInfo(): NetworkUserAccountInfo

    suspend fun updateUserAccountInfo(
        firstName: String,
        lastName: String,
    )
}

