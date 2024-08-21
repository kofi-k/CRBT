package com.example.crbtjetcompose.core.model.data


/**
 *  A [CrbtSongResource] with additional details such as whether the user
 *  is subscribed to the song or has gifted the song.
 * */
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
    val numberOfListeners: Int,
    val numberOfSubscribers: Int,
    val ussdCode: String,
    val subscriptionType: String,
    val price: String,
    val category: String,
    val isSubscribed: Boolean,
    val hasGiftedSong: Boolean,
)