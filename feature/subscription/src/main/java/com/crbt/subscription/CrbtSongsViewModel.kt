package com.crbt.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.musicService.PlayerState
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.domain.NextTrackUseCase
import com.crbt.domain.PauseMusicUseCase
import com.crbt.domain.PlayMusicUseCase
import com.crbt.domain.PreviousTrackUseCase
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class CrbtSongsViewModel @Inject constructor(
    private val playMusicUseCase: PlayMusicUseCase,
    private val pauseMusicUseCase: PauseMusicUseCase,
    private val nextTrackUseCase: NextTrackUseCase,
    private val previousTrackUseCase: PreviousTrackUseCase,
    crbtSongsRepository: UserCrbtMusicRepository,
) : ViewModel() {

    private var currentSongIndex: Int = 0

    private val _currentSong = MutableStateFlow<UserCRbtSongResource?>(null)
    val currentlyPlayingSong: StateFlow<UserCRbtSongResource?> = _currentSong.asStateFlow()
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    val playerState: StateFlow<PlayerState> = _playerState


    val crbtSongsFlow: StateFlow<CrbtSongsFeedUiState> =
        crbtSongsRepository.observeAllCrbtMusic()
            .map { results ->
                when (results) {
                    is CrbtSongsFeedUiState.Success -> {
                        CrbtSongsFeedUiState.Success(results.songs)
                    }

                    is CrbtSongsFeedUiState.Loading -> CrbtSongsFeedUiState.Loading
                    else -> CrbtSongsFeedUiState.Success(emptyList())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CrbtSongsFeedUiState.Loading
            )

    fun playOrPauseSong(song: UserCRbtSongResource) {
        if (_currentSong.value == song && _playerState.value == PlayerState.Playing) {
            pauseSong() // Pause if the song is currently playing
        } else {
            _currentSong.value = song
            playMusicUseCase(
                song,
                onBufferingUpdate = { bufferingPercent ->
                    _playerState.value = PlayerState.Buffering(bufferingPercent)
                },
                onError = { error ->
                    _playerState.value = PlayerState.Error(error)
                },
                onCompletion = {
                    _playerState.value = PlayerState.Idle
                    _currentSong.value = null
                }
            )
            _playerState.value = PlayerState.Playing
        }
    }

    // Pauses the current song
    fun pauseSong() {
        pauseMusicUseCase()
        _playerState.value = PlayerState.Paused
    }

    // Checks if the song is playing
    fun isSongPlaying(song: UserCRbtSongResource): Boolean {
        return _currentSong.value == song && _playerState.value == PlayerState.Playing
    }

    fun nextSong() {
        val currentSong = crbtSongsFlow.value
        currentSong as CrbtSongsFeedUiState.Success

        val nextIndex = (currentSongIndex + 1) % currentSong.songs.size
        _currentSong.value = currentSong.songs[nextIndex]

        nextTrackUseCase(
            _currentSong.value!!, // Ensure the correct song is passed
            onBufferingUpdate = { bufferingPercent ->
                _playerState.value = PlayerState.Buffering(bufferingPercent)
            },
            onError = { error ->
                _playerState.value = PlayerState.Error(error)
            },
            onCompletion = {
                _playerState.value = PlayerState.Idle
            }
        )
        _playerState.value = PlayerState.Playing
        currentSongIndex = nextIndex
    }


    fun previousSong() {
        val currentSong = crbtSongsFlow.value
        currentSong as CrbtSongsFeedUiState.Success
        val previousIndex =
            if (currentSongIndex - 1 >= 0) currentSongIndex - 1 else currentSong.songs.size - 1
        _currentSong.value = currentSong.songs[previousIndex]
        previousTrackUseCase(
            _currentSong.value!!,
            onBufferingUpdate = { bufferingPercent ->
                _playerState.value = PlayerState.Buffering(bufferingPercent)
            },
            onError = { error ->
                _playerState.value = PlayerState.Error(error)
            },
            onCompletion = {
                _playerState.value = PlayerState.Idle
            }
        )
        _playerState.value = PlayerState.Playing
        currentSongIndex = previousIndex
    }
}
