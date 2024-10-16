package com.crbt.subscription

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
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
    fun runUssdCode(
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
}