package com.crbt.services

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.repository.PackagesFeedUiState
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.domain.GetEthioPackagesUseCase
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UpdateUserBalanceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.crbt.services.navigation.TOPUP_AMOUNT_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: UssdRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val updateBalanceUseCase: UpdateUserBalanceUseCase,
    savedStateHandle: SavedStateHandle,
    getEthioPackagesUseCase: GetEthioPackagesUseCase
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


    val packagesFlow: StateFlow<PackagesFeedUiState> =
        getEthioPackagesUseCase()
            .map {
                when (it) {
                    is PackagesFeedUiState.Loading -> PackagesFeedUiState.Loading
                    is PackagesFeedUiState.Success -> PackagesFeedUiState.Success(it.feed)
                    is PackagesFeedUiState.Error -> PackagesFeedUiState.Success(emptyList())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PackagesFeedUiState.Loading
            )


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(
        ussdCode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        ussdType: CrbtUssdType,
        activity: Activity
    ) {
        viewModelScope.launch {
            repository.dialUssdCode(
                ussdCode = ussdCode,
                onSuccess = { message ->
                    onSuccess()
                    if (ussdType == CrbtUssdType.BALANCE_CHECK) {
                        viewModelScope.launch {
                            updateBalanceUseCase(message.extractBalance(message))
                        }
                    }
                },
                onFailure = {
                    onError(it)
                },
                activity = activity
            )
        }
    }


    fun onPhoneNumberChanged(phoneNumber: String, isValid: Boolean) {
        this.phoneNumber = phoneNumber
        this.isPhoneNumberValid = isValid
    }

}
