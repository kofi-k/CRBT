package com.example.crbtjetcompose.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class NetworkUserAccountInfo(
    val firstName: String,
    val lastName: String,
    val accountBalance: String,
    val phone: String,
    val langPref: String,
    val subSongDetails: SubSongDetails?,
    val rewardPoints: Int?
)

@Serializable
data class SubSongDetails(
    val artisteName: String,
    val songTitle: String,
    val subscriptionType: String,
    val price: String,
    val profile: String
)