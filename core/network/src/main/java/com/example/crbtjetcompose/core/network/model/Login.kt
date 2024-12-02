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
    val account: Account
)

@Serializable
data class Account(
    val accountBalance: String,
    val subSongId: Int,
    val id: Int,
    val phone: String,
    val langPref: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val rewardPoints: Int? = null
)