package com.crbt.data.core.data.repository

import com.crbt.common.core.common.result.Result
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import com.example.crbtjetcompose.core.model.data.mapToUserCrbtSongResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompositeUserCrbtSongsRepository @Inject constructor(
    private val crbtMusicRepository: CrbtMusicRepository,
    private val userPreferencesRepository: CrbtPreferencesRepository
) : UserCrbtMusicRepository {
    override fun observeAllCrbtMusic(filterInterestedLanguages: Set<String>?): Flow<CrbtSongsFeedUiState> =
        crbtMusicRepository.getCrbtMusic()
            .combine(userPreferencesRepository.userPreferencesData) { songs, userPreferences ->
                when (songs) {
                    is CrbtMusicResourceUiState.Success -> {
                        val crbtSongs = songs.songs.mapToUserCrbtSongResource(userPreferences)
                        CrbtSongsFeedUiState.Success(songs.songs)
                    }

                    is CrbtMusicResourceUiState.Error -> {
                        CrbtSongsFeedUiState.Error(songs.message)
                    }

                    is CrbtMusicResourceUiState.Loading -> {
                        CrbtSongsFeedUiState.Loading
                    }
                }
            }

    override fun observeLatestCrbtMusic(): Flow<Result<CrbtSongResource>> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        when (crbtSongsFeedUiState.songs.isEmpty()) {
                            true -> Result.Error(Exception("No songs found"))
                            false -> Result.Success(crbtSongsFeedUiState.songs.maxByOrNull { it.createdAt }!!)
                        }
                    }

                    is CrbtSongsFeedUiState.Error -> {
                        Result.Error(Exception(crbtSongsFeedUiState.errorMessage))
                    }

                    is CrbtSongsFeedUiState.Loading -> Result.Loading
                }
            }
}