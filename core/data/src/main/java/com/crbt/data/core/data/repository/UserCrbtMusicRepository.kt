package com.crbt.data.core.data.repository

import com.crbt.common.core.common.result.Result
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import kotlinx.coroutines.flow.Flow

interface UserCrbtMusicRepository {
    fun observeAllCrbtMusic(
        filterInterestedLanguages: Set<String>? = null,
    ): Flow<CrbtSongsFeedUiState>

    fun observeLatestCrbtMusic(): Flow<Result<CrbtSongResource>>
}