package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import kotlinx.serialization.Serializable


/**
 *  Network representation of [NetworkSongsResource] when fetched from /songs/all
 * */

@Serializable
data class NetworkSongsResource(
    val id: Int,
    val subServiceId: String?,
    val albumName: String?,
    val songTitle: String,
    val artisteName: String,
    val lang: String,
    val createdAt: String?,
    val profile: String,
    val tune: String?,
    val numberOfListeners: Int,
    val numberOfSubscribers: Int,
    val ussdCode: String,
    val subscriptionType: String,
    val price: String,
    val category: String
)

/**
 *  Convert [NetworkSongsResource] to [CrbtSongResource]
 *  todo create an entity for [NetworkSongsResource] and from the entity to [CrbtSongResource]
 * */

fun NetworkSongsResource.asExternalModel(): CrbtSongResource {
    return CrbtSongResource(
        id = id.toString(),
        songTitle = songTitle,
        artisteName = artisteName,
        tune = tune ?: "",
        profile = profile,
        subServiceId = subServiceId ?: "",
        albumName = albumName ?: "",
        lang = lang,
        createdAt = createdAt ?: "",
        numberOfListeners = numberOfListeners,
        numberOfSubscribers = numberOfSubscribers,
        ussdCode = ussdCode,
        subscriptionType = subscriptionType,
        price = price,
        category = category,
    )
}