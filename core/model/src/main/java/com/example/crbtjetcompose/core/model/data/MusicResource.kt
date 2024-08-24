package com.example.crbtjetcompose.core.model.data


data class MusicResource(
    val id: Long,
    val name: String,
    val data: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val coverArt: String?
)

