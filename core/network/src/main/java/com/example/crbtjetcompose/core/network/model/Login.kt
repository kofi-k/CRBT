package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val phone: String,
    val accountType: String,
    val langPref: String
)


@Serializable
data class LoginResponse(
    val message: String,
    val token: String,
    val account: UserAccountDetailsNetworkModel
)
