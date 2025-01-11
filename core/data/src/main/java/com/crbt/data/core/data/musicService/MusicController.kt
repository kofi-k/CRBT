package com.crbt.data.core.data.musicService

import com.crbt.data.core.data.PlayerState
import com.itengs.crbt.core.model.data.CrbtSongResource


interface MusicController {
    var mediaControllerCallback: (
        (
        playerState: PlayerState,
        currentMusic: CrbtSongResource?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )?

    fun addMediaItems(songs: List<CrbtSongResource>)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun getCurrentPosition(): Long

    fun destroy()

    fun skipToNextSong()

    fun skipToPreviousSong()

    fun getCurrentSong(): CrbtSongResource?

    fun seekTo(position: Long)
}