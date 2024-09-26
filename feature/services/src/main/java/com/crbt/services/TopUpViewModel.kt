package com.crbt.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor() : ViewModel() {

    var amount by mutableStateOf("")

    fun onAmountChange(newAmount: String) {
        amount = newAmount
    }
}