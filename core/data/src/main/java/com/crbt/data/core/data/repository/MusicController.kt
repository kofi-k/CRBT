package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.MusicResource

interface MusicController {


    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun getCurrentPosition(): Long

    fun destroy()

    fun skipToNextSong()

    fun skipToPreviousSong()

    fun getCurrentSong(): MusicResource?

    fun seekTo(position: Long)
}

