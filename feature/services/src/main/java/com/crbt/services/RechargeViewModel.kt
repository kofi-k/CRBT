package com.crbt.services

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class RechargeViewModel @Inject constructor(
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : ViewModel() {

    var voucherCodeState by mutableStateOf<VoucherCodeUiState>(VoucherCodeUiState.Idle)
        private set

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

    fun processImageUri(
        uri: Uri, context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        voucherCodeState = VoucherCodeUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requiredDigits = crbtPreferencesRepository.userPreferencesData.first()
                    .numberOfRechargeCodeDigits

                val code = extractVoucherCodeFromImage(uri, context, requiredDigits)
                voucherCodeState = if (code != null) {
                    onSuccess(code)
                    VoucherCodeUiState.Success(code)
                } else {
                    onError("No valid voucher code found")
                    VoucherCodeUiState.Error("No valid voucher code found")
                }
            } catch (e: Exception) {
                voucherCodeState = VoucherCodeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun captureImage(
        context: Context,
        imageCapture: ImageCapture,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        voucherCodeState = VoucherCodeUiState.Loading
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.cacheDir.resolve("captured_image.jpg")
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        processImageUri(uri, context, onSuccess, onError)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    voucherCodeState =
                        VoucherCodeUiState.Error(exception.message ?: "Failed to capture image")
                }
            }
        )
    }

    private suspend fun extractVoucherCodeFromImage(
        uri: Uri,
        context: Context,
        requiredDigits: Int
    ): String? = suspendCoroutine { continuation ->
        val image = InputImage.fromFilePath(context, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                val regex = "\\d{$requiredDigits}".toRegex()
                val matchedCode = result.textBlocks
                    .flatMap { it.lines }
                    .map { line -> line.text.replace("\\s".toRegex(), "") }
                    .firstNotNullOfOrNull { lineWithoutSpaces -> regex.find(lineWithoutSpaces)?.value }
                continuation.resume(matchedCode)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}

sealed class VoucherCodeUiState {
    data object Idle : VoucherCodeUiState()
    data object Loading : VoucherCodeUiState()
    data class Success(val voucherCode: String) : VoucherCodeUiState()
    data class Error(val message: String) : VoucherCodeUiState()
}