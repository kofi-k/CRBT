package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val error: String
)