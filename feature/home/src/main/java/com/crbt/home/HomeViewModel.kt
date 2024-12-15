package com.crbt.home

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtAdsUiState
import com.crbt.data.core.data.repository.HomeSongResource
import com.crbt.data.core.data.repository.HomeSongResourceState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.data.core.data.util.STOP_TIMEOUT
import com.crbt.domain.GetCrbtAdsUseCase
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UpdateUserBalanceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.example.crbtjetcompose.core.model.data.CrbtAdResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ussdRepository: UssdRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val updateBalanceUseCase: UpdateUserBalanceUseCase,
    crbtSongsRepository: UserCrbtMusicRepository,
    getCrbtAdsUseCase: GetCrbtAdsUseCase
) : ViewModel() {

    val ussdState: StateFlow<UssdUiState>
        get() = ussdRepository.ussdState

    private val reloadTrigger = MutableStateFlow(0)


    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        reloadTrigger.flatMapLatest {
            getUserDataPreferenceUseCase()
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
            )

    val songResourceUiState: StateFlow<HomeSongResourceState> =
        reloadTrigger.flatMapLatest {
            crbtSongsRepository.observeHomeResource()
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
                initialValue = HomeSongResourceState.Loading
            )


    val crbtAdsUiState: StateFlow<CrbtAdsUiState> =
        reloadTrigger.flatMapLatest {
            getCrbtAdsUseCase()
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
                initialValue = CrbtAdsUiState.Loading
            )


    @RequiresApi(Build.VERSION_CODES.O)
    fun runNewUssdCode(
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
                viewModelScope.launch {
                    updateBalanceUseCase(message.extractBalance(message))
                }
            },
            onFailure = {
                onError(it)
            }
        )
    }

    fun reloadHome() {
        reloadTrigger.value += 1
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val homeResource: HomeSongResource,
        val crbtAds: List<CrbtAdResource>
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}
