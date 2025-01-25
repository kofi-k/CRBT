package com.crbt.ui.core.ui.musicPlayer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.HomeSongResource
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.domain.AddCrbtSongsUseCase
import com.crbt.domain.PauseSongUseCase
import com.crbt.domain.PlaySongUseCase
import com.crbt.domain.ResumeSongUseCase
import com.crbt.domain.SkipToNextSongUseCase
import com.crbt.domain.SkipToPreviousSongUseCase
import com.itengs.crbt.core.model.data.CrbtSongResource
import com.itengs.crbt.core.model.data.LikeableToneCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val savedStateHandle: SavedStateHandle,
    private val crbtSongsRepository: UserCrbtMusicRepository,
) : ViewModel() {
    companion object {
        private const val SEARCH_QUERY_MIN_LENGTH = 3
        private const val SEARCH_QUERY = "searchQuery"
    }

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    private val _uiState = MutableStateFlow(TonesUiState())
    var uiState: StateFlow<TonesUiState> = _uiState.asStateFlow()


    fun onEvent(event: TonesPlayerEvent) {
        when (event) {
            TonesPlayerEvent.PlaySong -> playSong()
            TonesPlayerEvent.PauseSong -> pauseSong()
            TonesPlayerEvent.ResumeSong -> resumeSong()
            TonesPlayerEvent.FetchSong -> fetchSongs()
            is TonesPlayerEvent.OnSongSelected -> {
                _uiState.update { state ->
                    state.copy(selectedSong = event.selectedSong)
                }
            }

            is TonesPlayerEvent.SkipToNextSong -> skipToNextSong()
            is TonesPlayerEvent.SkipToPreviousSong -> skipToPreviousSong()
            is TonesPlayerEvent.OnSongSearch -> {
                savedStateHandle[SEARCH_QUERY] = event.query
                _uiState.update { state ->
                    when (event.query.length < SEARCH_QUERY_MIN_LENGTH) {
                        true -> state.copy(
                            searchResults = state.songs ?: emptyList()
                        )

                        else -> {
                            val filteredSongs =
                                state.songs?.filter { song ->
                                    song.songTitle.contains(event.query, ignoreCase = true) ||
                                            song.artisteName.contains(
                                                event.query,
                                                ignoreCase = true
                                            ) ||
                                            song.albumName.contains(event.query, ignoreCase = true)
                                }
                            state.copy(searchResults = filteredSongs ?: emptyList())
                        }
                    }
                }
            }
        }
    }

    fun songByToneId(toneId: String): CrbtSongResource? =
        _uiState.value.songs?.find { it.id == toneId }


    private fun fetchSongs() {
        viewModelScope.launch {
            crbtSongsRepository.observeAllCrbtMusic().collect { feed ->
                _uiState.update { state ->
                    when (feed) {
                        is CrbtSongsFeedUiState.Success -> {
                            addMediaItemsUseCase(feed.songs)
                            state.copy(
                                songs = feed.songs,
                                loading = false,
                                homeResource = HomeSongResource(
                                    popularTodaySongs = feed.songs
                                        .sortedByDescending { it.numberOfListeners }
                                        .take(8),
                                    latestSong = feed.songs.first(),
                                    currentUserCrbtSubscription = feed.currentUserCrbtSubscriptionSong,
                                ),
                                toneCategories = feed.toneCategories
                            )
                        }

                        is CrbtSongsFeedUiState.Error ->
                            state.copy(
                                errorMessage = feed.errorMessage,
                                loading = false,
                                homeResource = null,
                            )

                        is CrbtSongsFeedUiState.Loading ->
                            state.copy(
                                loading = true,
                                errorMessage = null,
                                homeResource = null,
                            )
                    }
                }
            }
        }
    }

    private fun playSong() {
        _uiState.value.apply {
            songs?.indexOf(selectedSong)?.let { song ->
                playSongUseCase(song)
            }
        }
    }

    private fun pauseSong() = pauseSongUseCase()

    private fun resumeSong() = resumeSongUseCase()

    private fun skipToNextSong() = skipToNextSongUseCase {
        _uiState.update { state ->
            state.copy(selectedSong = it)
        }
    }

    private fun skipToPreviousSong() = skipToPreviousSongUseCase {
        _uiState.update { state ->
            state.copy(selectedSong = it)
        }
    }
}

data class TonesUiState(
    val loading: Boolean? = false,
    val songs: List<CrbtSongResource>? = emptyList(),
    val selectedSong: CrbtSongResource? = null,
    val searchQuery: String? = null,
    val searchResults: List<CrbtSongResource> = emptyList(),
    val errorMessage: String? = null,
    val homeResource: HomeSongResource? = null,
    val toneCategories: List<LikeableToneCategory> = emptyList(),
)

fun List<CrbtSongResource>.findCurrentMusicControllerSong(
    tune: String
) = find { it.tune == tune } ?: firstOrNull()