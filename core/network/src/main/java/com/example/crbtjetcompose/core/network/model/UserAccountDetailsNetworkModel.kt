package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAccountDetailsNetworkModel(
    val accountBalance: String,
    val subSongId: Int,
    val id: Int,
    val phone: String,
    val langPref: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val rewardPoints: Int? = null,
    val location: String? = null,
    val profile: String? = null,
    val subSongDetails: SubSongDetails?,
)


@Serializable
data class SubSongDetails(
    val artisteName: String,
    val songTitle: String,
    val subscriptionType: String,
    val price: String,
    val profile: String
)