package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import javax.inject.Inject


class SkipToPreviousSongUseCase @Inject constructor(private val musicController: MusicController) {
    operator fun invoke(updateHomeUi: (CrbtSongResource?) -> Unit) {
        musicController.skipToPreviousSong()
        updateHomeUi(musicController.getCurrentSong())
    }
}