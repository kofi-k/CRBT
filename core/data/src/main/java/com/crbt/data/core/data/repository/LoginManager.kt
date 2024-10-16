package com.crbt.data.core.data.repository

import kotlinx.coroutines.flow.Flow

interface LoginManager {
    val isLoggedIn: Flow<Boolean>

    suspend fun isDifferentUser(phoneNumber: String): Boolean
}