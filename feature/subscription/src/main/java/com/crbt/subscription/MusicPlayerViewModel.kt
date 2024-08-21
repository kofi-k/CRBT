package com.crbt.subscription

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.musicService.MusicPlayer
import com.crbt.domain.GetMusicResourceUseCase
import com.example.crbtjetcompose.core.model.data.MusicResource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val getMusicResourceUseCase: GetMusicResourceUseCase,
    @ApplicationContext context: Context,
) : ViewModel() {
    private val _musicFiles = MutableStateFlow<List<MusicResource>>(emptyList())
    val musicFiles: StateFlow<List<MusicResource>> = _musicFiles

    private val _currentTrack = MutableStateFlow<MusicResource?>(null)
    val currentTrack: StateFlow<MusicResource?> = _currentTrack

    private val musicPlayer = MusicPlayer(context)

    init {
        loadMusicFiles()
    }

    fun loadMusicFiles() {
        viewModelScope.launch {
            val files = getMusicResourceUseCase.invoke().take(4)
            _musicFiles.value = files
            musicPlayer.setMusicFiles(files)
            // log the tracks and their indexes
            files.forEachIndexed { index, musicResource ->
                Log.d("MusicPlayerViewModel", "Track $index: ${musicResource.name}")
            }
        }
    }

    fun playPause() {
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause()
        } else {
            musicPlayer.play()
        }
        _currentTrack.value = musicPlayer.getCurrentTrack()
    }

    fun next() {
        musicPlayer.next()
        _currentTrack.value = musicPlayer.getCurrentTrack()
    }

    fun playTrack(index: Int) {
        musicPlayer.playTrack(index)
        _currentTrack.value = musicPlayer.getCurrentTrack()
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayer.release()
    }
}