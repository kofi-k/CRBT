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
import com.crbt.core.network.di.HttpException
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UserManager
import com.crbt.data.core.data.repository.UserPackageResources
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.findPackageDurationItemById
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.data.core.data.util.STOP_TIMEOUT
import com.crbt.data.core.data.util.generateGiftCrbtUssd
import com.crbt.domain.GetEthioPackagesUseCase
import com.crbt.subscription.navigation.GIFT_SUB_ARG
import com.crbt.subscription.navigation.TONE_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private val crbtNetworkRepository: CrbtNetworkRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    private val userManager: UserManager,
    getEthioPackagesUseCase: GetEthioPackagesUseCase
) : ViewModel() {

    val selectedTone: StateFlow<String?> = savedStateHandle.getStateFlow(TONE_ID_ARG, null)
    val isGiftSubscription: StateFlow<Boolean?> = savedStateHandle.getStateFlow(GIFT_SUB_ARG, null)

    var subscriptionUiState by mutableStateOf<SubscriptionUiState>(SubscriptionUiState.Idle)
        private set

    var ussdState: StateFlow<UssdUiState> = ussdRepository.ussdState


    val registrationPackagesFlow: StateFlow<com.crbt.common.core.common.result.Result<UserPackageResources?>> =
        getEthioPackagesUseCase.getUserRegistrationPackages()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
                initialValue = com.crbt.common.core.common.result.Result.Loading
            )


    val isUserRegisteredForCrbt =
        crbtPreferencesRepository.isUserRegisteredForCrbt
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
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        phoneNumber: String
    ) {
        subscriptionUiState = SubscriptionUiState.Loading
        val subscriptionCode = when (isGiftSubscription.value == true) {
            true -> try {
                val giftUssd = generateGiftCrbtUssd(ussdCode, phoneNumber)
                giftUssd
            } catch (e: IllegalArgumentException) {
                subscriptionUiState =
                    SubscriptionUiState.Error(e.message ?: "An error occurred")
                onError(e.message ?: "An error occurred")
                return
            }

            false -> ussdCode
        }


        runUssdCode(
            ussdCode = subscriptionCode,
            activity = activity,
            onSuccess = {
                handleUssdSuccess(
                    onSuccess = onSuccess,
                    onError = onError
                )
            },
            onError = { errorMessage ->
                onError(errorMessage)
                subscriptionUiState = SubscriptionUiState.Error(errorMessage)
            }
        )
    }

    private fun handleUssdSuccess(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            subscriptionUiState = try {
                val result = crbtNetworkRepository.subscribeToCrbt(selectedTone.value?.toInt() ?: 0)
                crbtPreferencesRepository.updateCrbtSubscriptionId(selectedTone.value?.toInt() ?: 0)
                userManager.getAccountInfo()
                onSuccess(result)
                SubscriptionUiState.Success(result)
            } catch (e: IOException) {
                onError("A network error occurred. Try again later.")
                when (e) {
                    is ConnectException -> SubscriptionUiState.Error("Oops! your internet connection seem to be off.")
                    is SocketTimeoutException -> SubscriptionUiState.Error("Hmm, connection timed out.")
                    is UnknownHostException -> SubscriptionUiState.Error("An error occurred while connecting to the server. Please try again later.")
                    else -> SubscriptionUiState.Error(
                        e.message ?: "A network error occurred. Please try again later"
                    )
                }
            } catch (e: HttpException) {
                onError(e.message ?: "An error occurred")
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
                SubscriptionUiState.Error(e.message ?: "An error occurred")
            }
        }
    }


    fun updateUserCrbtSubscriptionStatus(packageId: Int) {
        subscriptionUiState = SubscriptionUiState.Loading
        viewModelScope.launch {
            subscriptionUiState = try {
                registrationPackagesFlow.collect { data ->
                    when (data) {
                        is com.crbt.common.core.common.result.Result.Success -> {
                            crbtPreferencesRepository.setUserCrbtRegistrationStatus(
                                true,
                                data.data?.findPackageDurationItemById(packageId.toString()) ?: ""
                            )
                            userManager.getAccountInfo()
                            SubscriptionUiState.Success("")
                        }

                        is com.crbt.common.core.common.result.Result.Error -> {
                            SubscriptionUiState.Error(data.exception.message ?: "An error occurred")
                        }

                        else -> SubscriptionUiState.Loading
                    }

                }
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