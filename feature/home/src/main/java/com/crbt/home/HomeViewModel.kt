package com.crbt.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UssdRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase
) : ViewModel() {
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(
                ussdCode = ussdCode,
                onSuccess = {
                    onSuccess()
                    updateUserBalance()
                },
                onError
            )
        }
    }

    private fun updateUserBalance() {
        viewModelScope.launch {
            if (ussdState.value is UssdUiState.Success) {
                val balance = (ussdState.value as UssdUiState.Success).response.extractBalance()
                if (balance != null) {
                    crbtPreferencesRepository.setUserBalance(balance)
                }
            }
        }
    }
}