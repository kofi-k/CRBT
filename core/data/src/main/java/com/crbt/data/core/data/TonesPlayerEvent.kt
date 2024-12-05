package com.crbt.data.core.data

import com.example.crbtjetcompose.core.model.data.CrbtSongResource


sealed class TonesPlayerEvent {
    data object PlaySong : TonesPlayerEvent()
    data object PauseSong : TonesPlayerEvent()
    data object ResumeSong : TonesPlayerEvent()
    data object FetchSong : TonesPlayerEvent()
    data object SkipToNextSong : TonesPlayerEvent()
    data object SkipToPreviousSong : TonesPlayerEvent()
    data class OnSongSelected(val selectedSong: CrbtSongResource) : TonesPlayerEvent()
    data class OnSongSearch(val query: String) : TonesPlayerEvent()
}