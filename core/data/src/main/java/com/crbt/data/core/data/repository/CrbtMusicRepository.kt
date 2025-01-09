package com.crbt.data.core.data.repository

import com.itengs.crbt.core.model.data.CrbtSongResource
import kotlinx.coroutines.flow.Flow

interface CrbtMusicRepository {
    fun getCrbtMusic(): Flow<CrbtMusicResourceUiState>
}


sealed class CrbtMusicResourceUiState {
    data object Loading : CrbtMusicResourceUiState()
    data class Success(val songs: List<CrbtSongResource>) : CrbtMusicResourceUiState()
    data class Error(val message: String) : CrbtMusicResourceUiState()
}