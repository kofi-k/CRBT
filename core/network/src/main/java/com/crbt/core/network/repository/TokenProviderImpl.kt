package com.crbt.core.network.repository

import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val crbtPreferencesDataSource: CrbtPreferencesDataSource
) : TokenProvider {
    override suspend fun getToken(): String {
        return crbtPreferencesDataSource.userPreferencesData.first().token
    }

    override suspend fun setSystemUnderMaintenance(isUnderMaintenance: Boolean) =
        crbtPreferencesDataSource.setIsSystemUnderMaintenance(isUnderMaintenance)
}