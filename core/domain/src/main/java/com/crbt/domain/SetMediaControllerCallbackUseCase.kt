package com.crbt.domain

import com.crbt.data.core.data.PlayerState
import com.crbt.data.core.data.musicService.MusicController
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import javax.inject.Inject


class SetMediaControllerCallbackUseCase @Inject constructor(
    private val musicController: MusicController
) {
    operator fun invoke(
        callback: (
            playerState: PlayerState,
            currentSong: CrbtSongResource?,
            currentPosition: Long,
            totalDuration: Long,
            isShuffleEnabled: Boolean,
            isRepeatOneEnabled: Boolean
        ) -> Unit
    ) {
        musicController.mediaControllerCallback = callback
    }
}