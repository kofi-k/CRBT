package com.crbt.domain

import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import javax.inject.Inject

class UpdateUserBalanceUseCase @Inject constructor(
    private val crbtPreferencesRepository: CrbtPreferencesRepository
) {
    suspend operator fun invoke(newBalance: Double?) {
        if (newBalance != null) {
            crbtPreferencesRepository.setUserBalance(newBalance)
        }
    }
}