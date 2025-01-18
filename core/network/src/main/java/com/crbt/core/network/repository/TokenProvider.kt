package com.crbt.core.network.repository

interface TokenProvider {
    suspend fun getToken(): String?
    suspend fun setSystemUnderMaintenance(isUnderMaintenance: Boolean)
}
