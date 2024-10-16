package com.crbt.ui.core.ui.musicPlayer

sealed class CrbtSongEvent {
    data object PauseCrbtSong : CrbtSongEvent()
    data object ResumeCrbtSong : CrbtSongEvent()
    data object SkipToNextCrbtSong : CrbtSongEvent()
    data object SkipToPreviousCrbtSong : CrbtSongEvent()
    data class SeekCrbtSongToPosition(val position: Long) : CrbtSongEvent()
}