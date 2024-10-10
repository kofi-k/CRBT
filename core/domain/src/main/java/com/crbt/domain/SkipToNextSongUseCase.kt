package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import javax.inject.Inject


class SkipToNextSongUseCase @Inject constructor(private val musicController: MusicController) {
    operator fun invoke(updateHomeUi: (CrbtSongResource?) -> Unit) {
        musicController.skipToNextSong()
        updateHomeUi(musicController.getCurrentSong())
    }
}