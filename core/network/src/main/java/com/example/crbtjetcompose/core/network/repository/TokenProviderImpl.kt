package com.example.crbtjetcompose.core.network.repository

import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val crbtPreferencesDataSource: CrbtPreferencesDataSource
) : TokenProvider {
    override suspend fun getToken(): String {
        return crbtPreferencesDataSource.userPreferencesData.first().token
    }
}