package com.crbt.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.domain.AddCrbtSongsUseCase
import com.crbt.domain.PauseSongUseCase
import com.crbt.domain.PlaySongUseCase
import com.crbt.domain.ResumeSongUseCase
import com.crbt.domain.SkipToNextSongUseCase
import com.crbt.domain.SkipToPreviousSongUseCase
import com.crbt.subscription.navigation.TONE_ID_ARG
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CrbtTonesViewModel @Inject constructor(
    private val addMediaItemsUseCase: AddCrbtSongsUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    crbtSongsRepository: UserCrbtMusicRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val backstackCrbtMusic: StateFlow<String?> =
        savedStateHandle.getStateFlow(TONE_ID_ARG, null)

    var tonesUiState by mutableStateOf(TonesUiState())
        private set


    private val crbtSongsFlow: StateFlow<CrbtSongsFeedUiState> =
        crbtSongsRepository.observeAllCrbtMusic()
            .map { results ->
                when (results) {
                    is CrbtSongsFeedUiState.Success -> {
                        CrbtSongsFeedUiState.Success(results.songs)
                    }

                    is CrbtSongsFeedUiState.Loading -> CrbtSongsFeedUiState.Loading
                    else -> {
                        CrbtSongsFeedUiState.Success(emptyList())
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CrbtSongsFeedUiState.Loading
            )

    val crbtSongResource: StateFlow<CrbtSongResource?> =
        backstackCrbtMusic.flatMapLatest { toneId ->
            if (toneId != null) {
                crbtSongsRepository.observeAllCrbtMusic()
                    .map { songs ->
                        when (songs) {
                            is CrbtSongsFeedUiState.Error -> null
                            CrbtSongsFeedUiState.Loading -> null
                            is CrbtSongsFeedUiState.Success ->
                                songs.songs.find {
                                    it.id == toneId
                                }
                        }
                    }
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private fun getCrbtSongs() {
        viewModelScope.launch {
            crbtSongsFlow.collect { feed ->
                tonesUiState = when (feed) {
                    is CrbtSongsFeedUiState.Success -> {
                        addMediaItemsUseCase(feed.songs)
                        tonesUiState.copy(
                            songs = feed.songs,
                            loading = false,
                        )
//                            .apply {
//                            clickedCrbtMusic.map { id ->
//                                if (!id.isNullOrBlank()) {
//                                    println("Clicked crbt music: ${clickedCrbtMusic.value}")
//                                    tonesUiState =
//                                        tonesUiState.copy(selectedSong = songs?.find { it.id == clickedCrbtMusic.value })
//                                    playSong()
//                                }
//                            }
//                        }
                    }

                    is CrbtSongsFeedUiState.Error -> tonesUiState.copy(
                        errorMessage = feed.errorMessage,
                        loading = false
                    )

                    else -> tonesUiState.copy(loading = true, errorMessage = null)
                }
            }
        }
    }


    fun onEvent(event: TonesPlayerEvent) {
        when (event) {
            TonesPlayerEvent.PlaySong -> playSong()

            TonesPlayerEvent.PauseSong -> pauseSong()

            TonesPlayerEvent.ResumeSong -> resumeSong()

            TonesPlayerEvent.FetchSong -> getCrbtSongs()

            is TonesPlayerEvent.OnSongSelected -> tonesUiState =
                tonesUiState.copy(selectedSong = event.selectedSong)

            is TonesPlayerEvent.SkipToNextSong -> skipToNextSong()

            is TonesPlayerEvent.SkipToPreviousSong -> skipToPreviousSong()
        }
    }


    fun reload() = onEvent(TonesPlayerEvent.FetchSong)


    private fun playSong() {
        tonesUiState.apply {
            songs?.indexOf(selectedSong)?.let { song ->
                playSongUseCase(song)
            }
        }
    }

    private fun pauseSong() = pauseSongUseCase()

    private fun resumeSong() = resumeSongUseCase()

    private fun skipToNextSong() = skipToNextSongUseCase {
        tonesUiState = tonesUiState.copy(selectedSong = it)
    }

    private fun skipToPreviousSong() = skipToPreviousSongUseCase {
        tonesUiState = tonesUiState.copy(selectedSong = it)
    }
}

data class TonesUiState(
    val loading: Boolean? = false,
    val songs: List<CrbtSongResource>? = emptyList(),
    val selectedSong: CrbtSongResource? = null,
    val errorMessage: String? = null
)
