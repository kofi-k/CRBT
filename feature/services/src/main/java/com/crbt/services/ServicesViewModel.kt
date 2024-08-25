package com.crbt.services

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val repository: UssdRepository,
) : ViewModel() {
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    private var phoneNumber by mutableStateOf("")
    var isPhoneNumberValid by mutableStateOf(false)


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (Int) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(ussdCode, onSuccess, onError)
        }
    }


    fun onPhoneNumberChanged(phoneNumber: String, isValid: Boolean) {
        this.phoneNumber = phoneNumber
        this.isPhoneNumberValid = isValid
    }


}
