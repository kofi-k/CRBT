package com.crbt.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UssdRepository,
) : ViewModel() {
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(ussdCode, onSuccess, onError)
        }
    }

}