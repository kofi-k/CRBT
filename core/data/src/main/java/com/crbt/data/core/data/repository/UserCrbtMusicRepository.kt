package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import kotlinx.coroutines.flow.Flow

interface UserCrbtMusicRepository {
    fun observeAllCrbtMusic(
        filterInterestedLanguages: Set<String>? = null,
    ): Flow<CrbtSongsFeedUiState>


    fun observeHomeResource(): Flow<HomeSongResourceState>

    fun songByToneId(toneId: String): Flow<CrbtSongResource?>
}

sealed class HomeSongResourceState {
    data object Loading : HomeSongResourceState()
    data class Success(
        val resource: HomeSongResource
    ) : HomeSongResourceState()

    data class Error(val message: String) : HomeSongResourceState()
}


data class HomeSongResource(
    val popularTodaySongs: List<CrbtSongResource>,
    val latestSong: CrbtSongResource,
    val currentUserCrbtSubscription: CrbtSongResource?
)