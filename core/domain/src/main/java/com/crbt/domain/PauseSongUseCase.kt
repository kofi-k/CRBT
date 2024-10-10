package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import javax.inject.Inject

class PauseSongUseCase @Inject constructor(private val musicController: MusicController) {
    operator fun invoke() = musicController.pause()
}