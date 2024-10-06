package com.crbt.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.UssdRepository
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.repository.extractBalance
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UssdRepository,
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    crbtSongsRepository: UserCrbtMusicRepository,
) : ViewModel() {
    val ussdState: StateFlow<UssdUiState> get() = repository.ussdState

    val userPreferenceUiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            )

    val crbtSongsFlow: StateFlow<CrbtSongsFeedUiState> =
        crbtSongsRepository.observeAllCrbtMusic()
            .map { results ->
                when (results) {
                    is CrbtSongsFeedUiState.Success -> {
                        CrbtSongsFeedUiState.Success(results.songs)
                    }

                    is CrbtSongsFeedUiState.Loading -> CrbtSongsFeedUiState.Loading
                    else -> CrbtSongsFeedUiState.Success(emptyList())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CrbtSongsFeedUiState.Loading
            )

    val latestCrbtSong: StateFlow<Result<UserCRbtSongResource>> =
        crbtSongsRepository.oberveLatestCrbtMusic()
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Result.Loading
            )

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdCode(ussdCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            repository.runUssdCode(
                ussdCode = ussdCode,
                onSuccess = {
                    onSuccess()
                    updateUserBalance()
                },
                onError
            )
        }
    }

    private fun updateUserBalance() {
        viewModelScope.launch {
            if (ussdState.value is UssdUiState.Success) {
                val balance = (ussdState.value as UssdUiState.Success).response.extractBalance()
                if (balance != null) {
                    crbtPreferencesRepository.setUserBalance(balance)
                }
            }
        }
    }
}