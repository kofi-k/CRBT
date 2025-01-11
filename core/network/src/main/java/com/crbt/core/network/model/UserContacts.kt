package com.crbt.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserContacts(
    val contacts: List<String>
)