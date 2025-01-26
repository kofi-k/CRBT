package com.crbt.data.core.data.repository

import com.itengs.crbt.core.model.data.LikeableToneCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
                                .filter { song ->
                                    userPreferenceData.interestedToneCategories.isEmpty() || userPreferenceData.interestedToneCategories.contains(
                                        song.category
                                    )
                                }
                                .map {
                                    it.copy(
                                        subscriptionType = userPreferenceData.userCrbtRegistrationPackage
                                    )
                                },
                            currentUserCrbtSubscriptionSong = songs.songs.find {
                                it.id == userPreferenceData.currentCrbtSubscriptionId.toString()
                            },
                            toneCategories = songs.songs
                                .map { it.category }
                                .distinct()
                                .map { category ->
                                    LikeableToneCategory(
                                        toneCategory = category,
                                        isInterestedInCategory = userPreferenceData.interestedToneCategories.contains(
                                            category
                                        )
                                    )
                                }
                        )
                    }

                    is CrbtMusicResourceUiState.Error -> CrbtSongsFeedUiState.Error(songs.message)

                    is CrbtMusicResourceUiState.Loading -> CrbtSongsFeedUiState.Loading

                }
            }

}