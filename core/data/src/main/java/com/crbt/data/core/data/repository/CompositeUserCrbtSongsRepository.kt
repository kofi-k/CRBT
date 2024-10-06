package com.crbt.data.core.data.repository

import com.crbt.common.core.common.result.Result
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
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
                        CrbtSongsFeedUiState.Success(crbtSongs)
                    }

                    is CrbtMusicResourceUiState.Error -> {
                        CrbtSongsFeedUiState.Error(songs.message)
                    }

                    is CrbtMusicResourceUiState.Loading -> {
                        CrbtSongsFeedUiState.Loading
                    }
                }
            }

    override fun observeLatestCrbtMusic(): Flow<Result<UserCRbtSongResource>> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        Result.Success(crbtSongsFeedUiState.songs.maxByOrNull { it.date }!!)
                    }

                    is CrbtSongsFeedUiState.Error -> {
                        Result.Error(Exception(crbtSongsFeedUiState.errorMessage))
                    }

                    is CrbtSongsFeedUiState.Loading -> Result.Loading
                }
            }
}