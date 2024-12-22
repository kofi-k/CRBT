package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserInfo(
    val firstName: String,
    val lastName: String,
    val langPref: String,
    val profile: String?,
    val location: String,
)


@Serializable
data class AccountUpdatedResponse(
    val message: String,
    val updatedAccount: UserAccountDetailsNetworkModel
)