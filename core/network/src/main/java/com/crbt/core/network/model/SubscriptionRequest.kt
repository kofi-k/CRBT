package com.crbt.core.network.model

import kotlinx.serialization.Serializable

/**
 *  Network representation of [SubscriptionRequest] when made to /service/subscribe
 * */
@Serializable
data class SubscriptionRequest(
    val songId: String,
    val billingTime: String,
    val nextSubPayment: String
)

/**
 *  Network representation of [UnSubscriptionRequest] when made to /service/unsubscribe
 * */
@Serializable
data class UnSubscriptionRequest(
    val songId: String,
)


@Serializable
data class SubscriptionResponse(
    val message: String
)