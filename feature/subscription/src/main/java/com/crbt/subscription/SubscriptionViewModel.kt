package com.crbt.subscription

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.PermissionUtils
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UssdRepository,
) : ViewModel() {

    val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState


    val crbtSongResource = DummyTones.tones.find {
        it.id == selectedTone.value
    } ?: DummyTones.tones.first()


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (Int) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(ussdCode, onSuccess, onError)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissions(context: Context, onGranted: () -> Unit = {}) {
        val permissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO
        )
        val requestCode = 1

        PermissionUtils.checkAndRequestPermissions(context, permissions, requestCode, onGranted)
    }
}