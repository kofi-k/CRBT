package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import javax.inject.Inject


class SeekSongToPositionUseCase @Inject constructor(private val musicController: MusicController) {
    operator fun invoke(position: Long) {
        musicController.seekTo(position)
    }
}