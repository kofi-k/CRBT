package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val phone: String,
    val idToken: String,
)