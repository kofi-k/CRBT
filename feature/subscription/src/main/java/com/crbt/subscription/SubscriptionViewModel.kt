package com.crbt.subscription

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.PermissionUtils
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UssdRepository,
    crbtSongsRepository: UserCrbtMusicRepository
) : ViewModel() {

    private val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState


    val crbtSongResource: StateFlow<UserCRbtSongResource?> =
        selectedTone.flatMapLatest { toneId ->
            if (toneId != null) {
                crbtSongsRepository.observeAllCrbtMusic()
                    .map { songs ->
                        when (songs) {
                            is CrbtSongsFeedUiState.Error -> null
                            CrbtSongsFeedUiState.Loading -> null
                            is CrbtSongsFeedUiState.Success ->
                                songs.songs.find {
                                    it.id == toneId
                                }
                        }
                    }
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(ussdCode, onSuccess, onError)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissions(context: Context, onGranted: () -> Unit = {}) {
        val permissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_CONTACTS
        )
        val requestCode = 1

        PermissionUtils.checkAndRequestPermissions(context, permissions, requestCode, onGranted)
    }
}