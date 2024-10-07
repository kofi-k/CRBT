package com.example.crbtjetcompose.core.network.repository

import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest

interface CrbtNetworkRepository {
    suspend fun getSongs(): List<NetworkSongsResource>

    suspend fun subscribe(
        subscriptionRequest: SubscriptionRequest
    )

    suspend fun unsubscribe(
        subscriptionRequest: SubscriptionRequest
    )

    suspend fun login(
        phone: String,
        idToken: String
    )
}

