package com.crbt.subscription

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    crbtSongsRepository: UserCrbtMusicRepository,
    private val crbtNetworkRepository: CrbtNetworkRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
) : ViewModel() {

    private val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)
    private val _subscriptionUiState =
        MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Idle)
    var subscriptionUiState: StateFlow<SubscriptionUiState> = _subscriptionUiState.asStateFlow()


    val crbtSongResource: StateFlow<CrbtSongResource?> =
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

    val isUserOnCrbtSubscription: StateFlow<Boolean?> =
        crbtPreferencesRepository
            .userPreferencesData
            .flatMapLatest { userPreferencesData ->
                flowOf(userPreferencesData.currentCrbtSubscriptionId > 0)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )


    @RequiresApi(Build.VERSION_CODES.O)
    private fun runUssdCode(
        ussdCode: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        activity: Activity
    ) {
        repository.dialUssdCode(
            ussdCode = ussdCode,
            activity = activity,
            onSuccess = { message ->
                onSuccess(message)
            },
            onFailure = {
                onError(it)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun subscribeToTone(ussdCode: String, activity: Activity) {
        _subscriptionUiState.value = SubscriptionUiState.Loading
        runUssdCode(
            ussdCode = ussdCode,
            activity = activity,
            onSuccess = { handleUssdSuccess() },
            onError = { errorMessage ->
                _subscriptionUiState.value = SubscriptionUiState.Error(errorMessage)
            }
        )
    }


    private fun handleUssdSuccess() {
        viewModelScope.launch {
            _subscriptionUiState.value = try {
                val result = crbtNetworkRepository.subscribeToCrbt(selectedTone.value?.toInt() ?: 0)
                crbtPreferencesRepository.updateCrbtSubscriptionId(selectedTone.value?.toInt() ?: 0)
                SubscriptionUiState.Success(result)
            } catch (e: Exception) {
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class SubscriptionUiState {
    data object Idle : SubscriptionUiState()
    data object Loading : SubscriptionUiState()
    data class Error(val error: String) : SubscriptionUiState()
    data class Success(val message: String) : SubscriptionUiState()
}