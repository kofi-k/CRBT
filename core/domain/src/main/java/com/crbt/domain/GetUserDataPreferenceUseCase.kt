package com.crbt.domain

import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserDataPreferenceUseCase @Inject constructor(
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) {
    operator fun invoke(): Flow<UserPreferenceUiState> =
        crbtPreferencesRepository.userPreferencesData
            .map { UserPreferenceUiState.Success(it) }
}


sealed interface UserPreferenceUiState {
    data object Loading : UserPreferenceUiState
    data class Success(val userData: UserPreferencesData) : UserPreferenceUiState
}