package com.crbt.data.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserManager {
    val isLoggedIn: Flow<Boolean>

    suspend fun login(
        phone: String,
        accountType: String,
        langPref: String
    ): String

    suspend fun getAccountInfo()

    suspend fun updateUserInfo(
        firstName: String,
        lastName: String,
        profile: String?,
        email: String?,
    ): UpdateUserInfoUiState
}


sealed class UpdateUserInfoUiState {
    data object Loading : UpdateUserInfoUiState()
    data object Idle : UpdateUserInfoUiState()
    data object Success : UpdateUserInfoUiState()
    data class Error(val message: String) : UpdateUserInfoUiState()
}