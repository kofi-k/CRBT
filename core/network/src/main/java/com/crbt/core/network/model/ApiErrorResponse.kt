package com.crbt.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val error: String
)