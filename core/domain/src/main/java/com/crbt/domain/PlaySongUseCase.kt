package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import javax.inject.Inject

class PlaySongUseCase @Inject constructor(private val musicController: MusicController) {
    operator fun invoke(mediaItemIndex: Int) {
        musicController.play(mediaItemIndex)
    }
}