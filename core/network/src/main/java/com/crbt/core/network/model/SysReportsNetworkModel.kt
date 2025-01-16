package com.crbt.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SysReportsNetworkModel(
    val title: String,
    val description: String,
    val category: String,
)