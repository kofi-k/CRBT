package com.crbt.data.core.data.repository

import com.itengs.crbt.core.model.data.CrbtSongResource
import com.itengs.crbt.core.model.data.LikeableToneCategory

sealed interface CrbtSongsFeedUiState {
    data object Loading : CrbtSongsFeedUiState
    data class Success(
        val songs: List<CrbtSongResource>,
        val currentUserCrbtSubscriptionSong: CrbtSongResource?,
        val toneCategories: List<LikeableToneCategory>
    ) : CrbtSongsFeedUiState

    data class Error(val errorMessage: String) : CrbtSongsFeedUiState
}