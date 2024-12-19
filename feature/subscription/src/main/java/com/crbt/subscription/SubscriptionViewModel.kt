package com.crbt.subscription

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.SubscriptionBillingType
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.LoginManager
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.data.core.data.util.generateGiftCrbtUssd
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import com.example.crbtjetcompose.core.network.di.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val ussdRepository: UssdRepository,
    crbtSongsRepository: UserCrbtMusicRepository,
    private val crbtNetworkRepository: CrbtNetworkRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val loginManager: LoginManager
) : ViewModel() {

    private val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)
    private val _subscriptionUiState =
        MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Idle)
    var subscriptionUiState: StateFlow<SubscriptionUiState> = _subscriptionUiState.asStateFlow()

    var ussdState: StateFlow<UssdUiState> = ussdRepository.ussdState

    private var phoneNumber by mutableStateOf("")
    var crbtBillingType by mutableStateOf(SubscriptionBillingType.Monthly)


    val crbtSongResource: StateFlow<CrbtSongResource?> =
        selectedTone.flatMapLatest { toneId ->
            toneId?.let { crbtSongsRepository.songByToneId(it) } ?: flowOf(null)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    val isUserRegisteredForCrbt = crbtPreferencesRepository.isUserRegisteredForCrbt
        .mapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(
        ussdCode: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        activity: Activity
    ) {
        ussdRepository.dialUssdCode(
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
    fun subscribeToTone(
        ussdCode: String, activity: Activity,
    ) {
        _subscriptionUiState.value = SubscriptionUiState.Loading

        val subscriptionCode = when (isGiftSubscription.value == true) {
            true -> try {
                val giftUssd = generateGiftCrbtUssd(ussdCode, phoneNumber)
                giftUssd
            } catch (e: IllegalArgumentException) {
                _subscriptionUiState.value =
                    SubscriptionUiState.Error(e.message ?: "An error occurred")
                return
            }

            false -> ussdCode
        }


        runUssdCode(
            ussdCode = subscriptionCode,
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
                loginManager.getAccountInfo()
                SubscriptionUiState.Success(result)
            } catch (e: IOException) {
                when (e) {
                    is ConnectException -> SubscriptionUiState.Error("Oops! your internet connection seem to be off.")
                    is SocketTimeoutException -> SubscriptionUiState.Error("Hmm, connection timed out.")
                    is UnknownHostException -> SubscriptionUiState.Error("A network error occurred. Please check your connection and try again.")
                    else -> SubscriptionUiState.Error(e.message ?: "An error occurred")
                }
            } catch (e: HttpException) {
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            } catch (e: Exception) {
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            }
        }
    }


    fun updateUserCrbtSubscriptionStatus() {
        _subscriptionUiState.value = SubscriptionUiState.Loading
        viewModelScope.launch {
            _subscriptionUiState.value = try {
                crbtPreferencesRepository.setUserCrbtRegistrationStatus(true)
                SubscriptionUiState.Idle
            } catch (e: Exception) {
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun onPhoneNumberChange(number: String) {
        phoneNumber = number
    }

    fun onBillingTypeChange(billingType: SubscriptionBillingType) {
        crbtBillingType = billingType
    }

}

sealed class SubscriptionUiState {
    data object Idle : SubscriptionUiState()
    data object Loading : SubscriptionUiState()
    data class Error(val error: String) : SubscriptionUiState()
    data class Success(val message: String) : SubscriptionUiState()
}