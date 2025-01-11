package com.crbt.data.core.data.repository

import com.itengs.crbt.core.model.data.CrbtSongResource
import kotlinx.coroutines.flow.Flow

interface UserCrbtMusicRepository {
    fun observeAllCrbtMusic(
        filterInterestedLanguages: Set<String>? = null,
    ): Flow<CrbtSongsFeedUiState>


    fun songByToneId(toneId: String): Flow<CrbtSongResource?>
}


data class HomeSongResource(
    val popularTodaySongs: List<CrbtSongResource>,
    val latestSong: CrbtSongResource,
    val currentUserCrbtSubscription: CrbtSongResource?
)