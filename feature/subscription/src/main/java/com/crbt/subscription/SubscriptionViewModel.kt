package com.crbt.subscription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.crbt.data.core.data.DummyTones
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)

    val crbtSongResource = DummyTones.tones.find {
        it.id == selectedTone.value
    } ?: DummyTones.tones.first()
}