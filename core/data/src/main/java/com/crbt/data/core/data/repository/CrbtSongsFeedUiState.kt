package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.CrbtSongResource

sealed interface CrbtSongsFeedUiState {
    data object Loading : CrbtSongsFeedUiState
    data class Success(val songs: List<CrbtSongResource>) : CrbtSongsFeedUiState
    data class Error(val errorMessage: String) : CrbtSongsFeedUiState
}