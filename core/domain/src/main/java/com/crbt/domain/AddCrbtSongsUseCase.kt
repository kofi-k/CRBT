package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicController
import com.itengs.crbt.core.model.data.CrbtSongResource
import javax.inject.Inject

class AddCrbtSongsUseCase @Inject constructor(
    private val musicController: MusicController,
) {
    operator fun invoke(songs: List<CrbtSongResource>) {
        musicController.addMediaItems(songs)
    }
}