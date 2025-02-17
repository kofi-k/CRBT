package com.crbt.data.core.data.repository.network

import com.crbt.core.network.model.AccountUpdatedResponse
import com.crbt.core.network.model.CrbtNetworkAds
import com.crbt.core.network.model.CrbtNetworkPackage
import com.crbt.core.network.model.LoginResponse
import com.crbt.core.network.model.NetworkPackageItem
import com.crbt.core.network.model.NetworkSongsResource
import com.crbt.core.network.model.SubscriptionRequest
import com.crbt.core.network.model.UserAccountDetailsNetworkModel

interface CrbtNetworkRepository {
    suspend fun getSongs(): List<NetworkSongsResource>

    suspend fun getAds(): List<CrbtNetworkAds>

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

    suspend fun getUserAccountInfo(): UserAccountDetailsNetworkModel

    suspend fun updateUserAccountInfo(
        firstName: String,
        lastName: String,
        profile: String?,
        email: String?,
        location: String
    ): AccountUpdatedResponse

    suspend fun uploadUserContacts(contacts: List<String>)
}

