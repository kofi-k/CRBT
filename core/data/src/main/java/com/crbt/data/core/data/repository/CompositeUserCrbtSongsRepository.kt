package com.crbt.data.core.data.repository

import com.crbt.common.core.common.result.Result
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
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
            .combine(userPreferencesRepository.userPreferencesData) { songs, _ ->
                when (songs) {
                    is CrbtMusicResourceUiState.Success -> {
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

    override fun observePopularTodayCrbtMusic(): Flow<CrbtSongsFeedUiState> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        when (crbtSongsFeedUiState.songs.isEmpty()) {
                            true -> CrbtSongsFeedUiState.Error("No songs found")
                            false -> CrbtSongsFeedUiState.Success(crbtSongsFeedUiState.songs.sortedByDescending { it.createdAt }
                                .take(8))
                        }
                    }

                    is CrbtSongsFeedUiState.Error -> {
                        CrbtSongsFeedUiState.Error(crbtSongsFeedUiState.errorMessage)
                    }

                    is CrbtSongsFeedUiState.Loading -> {
                        CrbtSongsFeedUiState.Loading
                    }
                }
            }

    override fun observeUserCrbtSubscription(): Flow<Result<CrbtSongResource?>> =
        userPreferencesRepository.userPreferencesData
            .combine(observeAllCrbtMusic()) { userPreferences, crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        val userCrbtSong =
                            crbtSongsFeedUiState.songs.find { it.id == userPreferences.currentCrbtSubscriptionId.toString() }
                        when (userCrbtSong) {
                            null -> Result.Error(Exception("You have no active CRBT subscription"))
                            else -> Result.Success(userCrbtSong)
                        }
                    }

                    is CrbtSongsFeedUiState.Error -> {
                        Result.Error(Exception(crbtSongsFeedUiState.errorMessage))
                    }

                    is CrbtSongsFeedUiState.Loading -> Result.Loading
                }
            }
}