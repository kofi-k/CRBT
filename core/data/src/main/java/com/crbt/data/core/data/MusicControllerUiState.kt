package com.crbt.data.core.data

import com.example.crbtjetcompose.core.model.data.CrbtSongResource

data class MusicControllerUiState(
    val playerState: PlayerState? = null,
    val currentSong: CrbtSongResource? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)