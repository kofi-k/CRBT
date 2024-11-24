package com.crbt.data.core.data.repository

import kotlinx.coroutines.flow.Flow

sealed class RefreshUiState {
    data object Loading : RefreshUiState()
    data object Success : RefreshUiState()
    data class Error(val message: String) : RefreshUiState()
}

interface RefreshRepository {

    suspend fun refreshUserInfo(): Flow<RefreshUiState>

    suspend fun refreshSongs(): Flow<RefreshUiState>

    suspend fun refreshAds(): Flow<RefreshUiState>

    suspend fun refreshPackages(): Flow<RefreshUiState>

}
