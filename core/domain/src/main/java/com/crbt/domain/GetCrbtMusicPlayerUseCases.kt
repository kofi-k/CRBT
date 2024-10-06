package com.crbt.domain

import com.crbt.data.core.data.musicService.MusicPlayer
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
import javax.inject.Inject


class PlayMusicUseCase @Inject constructor(
    private val musicPlayer: MusicPlayer
) {
    operator fun invoke(
        song: UserCRbtSongResource,
        onBufferingUpdate: (Int) -> Unit,
        onError: (String) -> Unit,
        onCompletion: () -> Unit
    ) {
        musicPlayer.play(
            url = song.song,
            onBufferingUpdate = onBufferingUpdate,
            onError = onError,
            onCompletion = onCompletion
        )
    }
}

class PauseMusicUseCase @Inject constructor(
    private val musicPlayer: MusicPlayer
) {
    operator fun invoke() {
        musicPlayer.pause()
    }
}

class NextTrackUseCase @Inject constructor(
    private val musicPlayer: MusicPlayer,
) {
    operator fun invoke(
        nextSong: UserCRbtSongResource,
        onBufferingUpdate: (Int) -> Unit,
        onError: (String) -> Unit,
        onCompletion: () -> Unit
    ) {
        musicPlayer.play(
            url = nextSong.song,
            onBufferingUpdate = onBufferingUpdate,
            onError = onError,
            onCompletion = onCompletion
        )
    }
}

class PreviousTrackUseCase @Inject constructor(
    private val musicPlayer: MusicPlayer
) {
    operator fun invoke(
        previousSong: UserCRbtSongResource,
        onBufferingUpdate: (Int) -> Unit,
        onError: (String) -> Unit,
        onCompletion: () -> Unit
    ) {
        musicPlayer.play(
            previousSong.song,
            onBufferingUpdate = onBufferingUpdate,
            onError = onError,
            onCompletion = onCompletion
        )
    }
}
