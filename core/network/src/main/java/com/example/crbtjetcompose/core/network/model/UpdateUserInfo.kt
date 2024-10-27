package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserInfo(
    val firstName: String,
    val lastName: String,
)
