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

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")


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

    val filteredSongsFlow: StateFlow<List<CrbtSongResource>> = searchQuery
        .combine(crbtSongsRepository.observeAllCrbtMusic()) { query, results ->
            val allSongs = (results as? CrbtSongsFeedUiState.Success)?.songs ?: emptyList()
            if (query.length >= SEARCH_QUERY_MIN_LENGTH) {
                allSongs.filter { it.songTitle.contains(query, ignoreCase = true) }
            } else {
                allSongs
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
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

    fun onSearchQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
        // filter songs in tonesUiState
        if (query.length >= SEARCH_QUERY_MIN_LENGTH) {
            tonesUiState = tonesUiState.copy(
                songs = tonesUiState.songs?.filter {
                    it.songTitle.contains(query, ignoreCase = true)
                }
            )
        } else {
            reload()
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

private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_QUERY = "searchQuery"