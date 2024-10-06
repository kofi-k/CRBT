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
            .map {
                when (isCurrentPhoneNumberEmpty()) {
                    true -> {
                        crbtPreferencesRepository.setPhoneNumber(phoneNumber)
                        return@map true
                    }

                    false -> {
                        val isDifferentUser = it.phoneNumber != phoneNumber
                        if (isDifferentUser) {
                            crbtPreferencesRepository.clearUserPreferences()
                            crbtPreferencesRepository.setPhoneNumber(phoneNumber)
                        }
                        isDifferentUser
                    }
                }
            }
            .first()
    }

    private suspend fun isCurrentPhoneNumberEmpty(): Boolean {
        return crbtPreferencesRepository.userPreferencesData
            .map { it.phoneNumber.isBlank() }
            .first()
    }

}