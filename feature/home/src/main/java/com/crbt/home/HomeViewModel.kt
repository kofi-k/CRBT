package com.crbt.home

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UpdateUserBalanceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ussdRepository: UssdRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val updateBalanceUseCase: UpdateUserBalanceUseCase,
    crbtSongsRepository: UserCrbtMusicRepository,
) : ViewModel() {

    val newUssdState: StateFlow<UssdUiState>
        get() = ussdRepository.ussdState

    private var selectedTab by mutableStateOf(PopularTodayOptions.Tones)


    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )

    val crbtSongsFlow: StateFlow<CrbtSongsFeedUiState> =
        crbtSongsRepository.observePopularTodayCrbtMusic()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CrbtSongsFeedUiState.Loading
            )

    val latestCrbtSong: StateFlow<Result<CrbtSongResource>> =
        crbtSongsRepository.observeLatestCrbtMusic()
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Result.Loading
            )

    val currentUserCrbtSubscription: StateFlow<Result<CrbtSongResource?>> =
        crbtSongsRepository.observeUserCrbtSubscription()
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Result.Loading
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

    fun onPopularTodayTabChange(tab: PopularTodayOptions) {
        selectedTab = tab
    }
}