package com.crbt.data.core.data.repository

import com.itengs.crbt.core.model.data.CrbtSongResource
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
                            songs = songs.songs
                                .sortedByDescending { song -> song.createdAt }
                                .map {
                                    it.copy(
                                        subscriptionType = userPreferenceData.userCrbtRegistrationPackage
                                    )
                                },
                            currentUserCrbtSubscriptionSong = songs.songs.find {
                                it.id == userPreferenceData.currentCrbtSubscriptionId.toString()
                            }
                        )
                    }

                    is CrbtMusicResourceUiState.Error -> CrbtSongsFeedUiState.Error(songs.message)

                    is CrbtMusicResourceUiState.Loading -> CrbtSongsFeedUiState.Loading

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

}