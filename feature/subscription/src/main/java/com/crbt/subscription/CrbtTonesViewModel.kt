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
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val SEARCH_QUERY_MIN_LENGTH = 2
        private const val SEARCH_QUERY = "searchQuery"
    }

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    var tonesUiState by mutableStateOf(TonesUiState())
        private set

    private val allSongsFlow: StateFlow<CrbtSongsFeedUiState> =
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

    val filteredSongsFlow = combine(allSongsFlow, searchQuery) { songs, query ->
        if (query.length < SEARCH_QUERY_MIN_LENGTH) songs
        else {
            when (songs) {
                is CrbtSongsFeedUiState.Success -> {
                    val filteredSongs =
                        songs.songs.filter { song ->
                            song.songTitle.contains(query, ignoreCase = true) ||
                                    song.artisteName.contains(query, ignoreCase = true) ||
                                    song.albumName.contains(query, ignoreCase = true)
                        }
                    CrbtSongsFeedUiState.Success(filteredSongs)
                }

                else -> songs
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CrbtSongsFeedUiState.Loading
    )


    fun onEvent(event: TonesPlayerEvent) {
        when (event) {
            TonesPlayerEvent.PlaySong -> playSong()
            TonesPlayerEvent.PauseSong -> pauseSong()
            TonesPlayerEvent.ResumeSong -> resumeSong()
            TonesPlayerEvent.FetchSong -> fetchSongs()
            is TonesPlayerEvent.OnSongSelected -> tonesUiState =
                tonesUiState.copy(selectedSong = event.selectedSong)

            is TonesPlayerEvent.SkipToNextSong -> skipToNextSong()
            is TonesPlayerEvent.SkipToPreviousSong -> skipToPreviousSong()
            is TonesPlayerEvent.OnSongSearch -> {
                savedStateHandle[SEARCH_QUERY] = event.query
                viewModelScope.launch {
                    filteredSongsFlow.collect { searchResults ->
                        tonesUiState = when (searchResults) {
                            is CrbtSongsFeedUiState.Success -> tonesUiState.copy(
                                searchResults = searchResults.songs,
                                loading = false
                            )

                            is CrbtSongsFeedUiState.Error -> tonesUiState.copy(
                                errorMessage = searchResults.errorMessage,
                                loading = false
                            )

                            else -> tonesUiState.copy(loading = true, errorMessage = null)
                        }
                    }
                }
            }
        }
    }

    private fun fetchSongs() {
        viewModelScope.launch {
            allSongsFlow.collect { feed ->
                tonesUiState = when (feed) {
                    is CrbtSongsFeedUiState.Success -> {
                        addMediaItemsUseCase(feed.songs)
                        tonesUiState.copy(
                            songs = feed.songs,
                            loading = false,
                        )
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
    val searchQuery: String? = null,
    val searchResults: List<CrbtSongResource>? = emptyList(),
    val errorMessage: String? = null
)

fun List<CrbtSongResource>.findCurrentMusicControllerSong(
    tune: String
) = find { it.tune == tune } ?: firstOrNull()