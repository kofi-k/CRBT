package com.itengs.crbt.core.model.data


data class CrbtSongResource(
    val id: String,
    val songTitle: String,
    val artisteName: String,
    val tune: String,
    val profile: String,
    val subServiceId: String,
    val albumName: String,
    val lang: String,
    val createdAt: String,
    val numberOfListeners: Int,
    val numberOfSubscribers: Int,
    val ussdCode: String,
    val subscriptionType: String,
    val price: String,
    val category: String,
    val registrationUssdCode: String,
)