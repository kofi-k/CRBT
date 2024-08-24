package com.example.crbtjetcompose.core.network.repository

import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import com.example.crbtjetcompose.core.network.retrofit.RetrofitCrbtNetworkApi
import javax.inject.Inject

class CrbtNetworkRepositoryImpl @Inject constructor(
    private val retrofitCrbtNetworkApi: RetrofitCrbtNetworkApi
) : CrbtNetworkRepository {
    override suspend fun getSongs(page: Int, limit: Int): List<NetworkSongsResource> {
        return retrofitCrbtNetworkApi.getSongs(page, limit).results
    }

    override suspend fun subscribe(subscriptionRequest: SubscriptionRequest) {
        retrofitCrbtNetworkApi.subscribe(subscriptionRequest)
    }

    override suspend fun unsubscribe(subscriptionRequest: SubscriptionRequest) {
        retrofitCrbtNetworkApi.unsubscribe(subscriptionRequest)
    }
}