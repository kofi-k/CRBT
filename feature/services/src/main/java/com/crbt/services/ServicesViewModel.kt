package com.crbt.services

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.crbt.services.navigation.TOPUP_AMOUNT_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: UssdRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val topUpAmount: StateFlow<String?> =
        savedStateHandle.getStateFlow(TOPUP_AMOUNT_ARG, null)
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.Eagerly,
            )

    var phoneNumber by mutableStateOf("")
    var isPhoneNumberValid by mutableStateOf(false)


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(
        ussdCode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        ussdType: CrbtUssdType
    ) {
        viewModelScope.launch {
            repository.runUssdCode(
                ussdCode = ussdCode,
                onSuccess = {
                    onSuccess()
                    if (ussdType == CrbtUssdType.BALANCE_CHECK) {
                        updateUserBalance()
                    }
                },
                onError
            )
        }
    }


    fun onPhoneNumberChanged(phoneNumber: String, isValid: Boolean) {
        this.phoneNumber = phoneNumber
        this.isPhoneNumberValid = isValid
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
