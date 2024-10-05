package com.crbt.data.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CrbtUserMonitor @Inject constructor(
    private val crbtPreferencesRepository: CrbtPreferencesRepository
) : LoginManager {
    override val isLoggedIn: Flow<Boolean>
        get() = crbtPreferencesRepository.userPreferencesData.map { it.isUserSignedIn }

    override suspend fun isDifferentUser(phoneNumber: String): Boolean {
        return crbtPreferencesRepository.userPreferencesData
            .map { it.phoneNumber != phoneNumber }
            .first()
    }
}