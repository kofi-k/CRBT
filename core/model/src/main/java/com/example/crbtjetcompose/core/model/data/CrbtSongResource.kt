package com.example.crbtjetcompose.core.model.data


data class CrbtSongResource(
    val id: String,
    val songTitle: String,
    val artisteName: String,
    val song: String,
    val profile: String,
    val subServiceId: String,
    val albumName: String,
    val lang: String,
    val date: String,
    val numberOfListeners: List<Int>,
    val numberOfSubscribers: List<Int>,
    val ussdCode: String,
    val subscriptionType: String,
    val price: String,
    val category: String,
)