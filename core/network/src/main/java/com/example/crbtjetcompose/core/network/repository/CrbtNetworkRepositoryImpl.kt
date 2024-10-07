package com.example.crbtjetcompose.core.network.repository

import com.example.crbtjetcompose.core.network.model.Login
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import com.example.crbtjetcompose.core.network.retrofit.RetrofitCrbtNetworkApi
import javax.inject.Inject

class CrbtNetworkRepositoryImpl @Inject constructor(
    private val retrofitCrbtNetworkApi: RetrofitCrbtNetworkApi
) : CrbtNetworkRepository {
    override suspend fun getSongs(): List<NetworkSongsResource> {
        return retrofitCrbtNetworkApi.getSongs().allSongs
    }

    override suspend fun subscribe(subscriptionRequest: SubscriptionRequest) {
        retrofitCrbtNetworkApi.subscribe(subscriptionRequest)
    }

    override suspend fun unsubscribe(subscriptionRequest: SubscriptionRequest) {
        retrofitCrbtNetworkApi.unsubscribe(subscriptionRequest)
    }

    override suspend fun login(phone: String, idToken: String) {
        retrofitCrbtNetworkApi.login(Login(phone, idToken))
    }
}