package com.crbt.services

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RechargeViewModel @Inject constructor(
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : ViewModel() {
    private val _voucherCodeState: MutableStateFlow<VoucherCodeUiState> =
        MutableStateFlow(VoucherCodeUiState.Idle)
    val voucherCodeState: StateFlow<VoucherCodeUiState> = _voucherCodeState


    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000L),
            )


    fun onRechargeCodeChanged(requiredDigits: Int) {
        viewModelScope.launch {
            crbtPreferencesRepository.setRequiredRechargeDigits(requiredDigits)
        }
    }

    fun onAutoDialEnabledChanged(isAutoDialEnabled: Boolean) {
        viewModelScope.launch {
            crbtPreferencesRepository.setAutoDialRechargeCode(isAutoDialEnabled)
        }
    }

    fun processImageUri(uri: Uri, context: Context) {
        _voucherCodeState.value = VoucherCodeUiState.Loading
        viewModelScope.launch {
            val code = extractVoucherCodeFromImage(uri, context)
            _voucherCodeState.value = if (code != null) {
                VoucherCodeUiState.Success(code)
            } else {
                VoucherCodeUiState.Error("Failed to extract voucher code")
            }
        }
    }

    private fun extractVoucherCodeFromImage(uri: Uri, context: Context): String? {
        val image = InputImage.fromFilePath(context, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        return try {
            val result = recognizer.process(image).result
            result.textBlocks.firstOrNull()?.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}

sealed class VoucherCodeUiState {
    data object Idle : VoucherCodeUiState()
    data object Loading : VoucherCodeUiState()
    data class Success(val voucherCode: String) : VoucherCodeUiState()
    data class Error(val message: String) : VoucherCodeUiState()
}