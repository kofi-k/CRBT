package com.crbt.data.core.data.mapper

import androidx.media3.common.MediaItem
import com.example.crbtjetcompose.core.model.data.CrbtSongResource

fun MediaItem.toSongResource() =
    CrbtSongResource(
        id = mediaId,
        songTitle = mediaMetadata.title.toString(),
        tune = mediaId,
        profile = mediaMetadata.artworkUri.toString(),
        albumName = mediaMetadata.albumTitle.toString(),
        lang = "",
        createdAt = "",
        numberOfListeners = 0,
        numberOfSubscribers = 0,
        ussdCode = "",
        subscriptionType = "",
        price = "",
        category = "",
        artisteName = mediaMetadata.artist.toString(),
        subServiceId = ""
    )