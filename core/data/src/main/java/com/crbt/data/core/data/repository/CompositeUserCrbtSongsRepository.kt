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
            .combine(userPreferencesRepository.userPreferencesData) { songs, userPreferenceData ->
                when (songs) {
                    is CrbtMusicResourceUiState.Success -> {
                        CrbtSongsFeedUiState.Success(
                            songs = songs.songs,
                            currentUserCrbtSubscriptionSong = songs.songs.find {
                                it.id == userPreferenceData.currentCrbtSubscriptionId.toString()
                            }
                        )
                    }

                    is CrbtMusicResourceUiState.Error -> CrbtSongsFeedUiState.Error(songs.message)

                    is CrbtMusicResourceUiState.Loading -> CrbtSongsFeedUiState.Loading

                }
            }

    override fun observeHomeResource(): Flow<HomeSongResourceState> =
        combine(
            observePopularTodayCrbtMusic(),
            observeLatestCrbtMusic(),
            observeUserCrbtSubscription()
        ) { popularTodaySongs, latestSong, currentUserCrbtSubscription ->
            when {
                popularTodaySongs is CrbtSongsFeedUiState.Success &&
                        latestSong is Result.Success &&
                        currentUserCrbtSubscription is Result.Success -> {
                    HomeSongResourceState.Success(
                        HomeSongResource(
                            popularTodaySongs.songs,
                            latestSong.data,
                            currentUserCrbtSubscription.data
                        )
                    )
                }

                popularTodaySongs is CrbtSongsFeedUiState.Error -> {
                    HomeSongResourceState.Error(popularTodaySongs.errorMessage)
                }

                latestSong is Result.Error -> {
                    HomeSongResourceState.Error(latestSong.exception.message ?: "An error occurred")
                }

                currentUserCrbtSubscription is Result.Error -> {
                    HomeSongResourceState.Error(
                        currentUserCrbtSubscription.exception.message ?: "An error occurred"
                    )
                }

                else -> HomeSongResourceState.Loading
            }
        }

    override fun songByToneId(toneId: String): Flow<CrbtSongResource?> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success ->
                        crbtSongsFeedUiState.songs.find {
                            it.id == toneId
                        }

                    else -> null
                }
            }

    private fun observeLatestCrbtMusic(): Flow<Result<CrbtSongResource>> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        when (crbtSongsFeedUiState.songs.isEmpty()) {
                            true -> Result.Error(Exception("No songs found"))
                            false -> Result.Success(crbtSongsFeedUiState.songs.maxByOrNull { it.createdAt }!!)
                        }
                    }

                    else -> Result.Loading
                }
            }

    private fun observePopularTodayCrbtMusic(): Flow<CrbtSongsFeedUiState> =
        observeAllCrbtMusic()
            .map { crbtSongsFeedUiState ->
                when (crbtSongsFeedUiState) {
                    is CrbtSongsFeedUiState.Success -> {
                        when (crbtSongsFeedUiState.songs.isEmpty()) {
                            true -> CrbtSongsFeedUiState.Error("No songs found")
                            false -> CrbtSongsFeedUiState.Success(
                                crbtSongsFeedUiState.songs.sortedByDescending { it.createdAt }
                                    .take(8),
                                currentUserCrbtSubscriptionSong = crbtSongsFeedUiState.currentUserCrbtSubscriptionSong
                            )
                        }
                    }

                    else -> crbtSongsFeedUiState
                }
            }

    private fun observeUserCrbtSubscription(): Flow<Result<CrbtSongResource?>> =
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

                    else -> Result.Loading
                }
            }

}