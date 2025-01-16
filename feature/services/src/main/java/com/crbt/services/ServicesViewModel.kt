package com.crbt.services

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.repository.PackagesFeedUiState
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.data.core.data.util.STOP_TIMEOUT
import com.crbt.domain.GetEthioPackagesUseCase
import com.crbt.domain.UpdateUserBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: UssdRepository,
    private val updateBalanceUseCase: UpdateUserBalanceUseCase,
    getEthioPackagesUseCase: GetEthioPackagesUseCase
) : ViewModel() {

    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    private val reloadTrigger = MutableStateFlow(0)


    val packagesFlow: StateFlow<PackagesFeedUiState> =
        reloadTrigger.flatMapLatest {
            getEthioPackagesUseCase()
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
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


    fun reloadPackages() {
        reloadTrigger.value += 1
    }

}
