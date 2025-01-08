package com.example.crbtjetcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.RefreshRepository
import com.crbt.data.core.data.repository.RefreshUiState
import com.crbt.domain.GetUserDataPreferenceUseCase
import com.crbt.domain.UserPreferenceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getUserDataPreferenceUseCase: GetUserDataPreferenceUseCase,
    private val refreshRepository: RefreshRepository
) : ViewModel() {
    private val _refreshUiState = MutableStateFlow<RefreshUiState>(RefreshUiState.Success)
    val refreshUiState: StateFlow<RefreshUiState> get() = _refreshUiState


    val uiState: StateFlow<UserPreferenceUiState> =
        getUserDataPreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                initialValue = UserPreferenceUiState.Loading,
                started = SharingStarted.Eagerly,
            )

    fun refreshUserInfo() {
        viewModelScope.launch {
            refreshRepository.refreshUserInfo().collect {
                _refreshUiState.value = it
            }
        }
    }

    fun refreshSongs() {
        viewModelScope.launch {
            refreshRepository.refreshSongs().collect {
                _refreshUiState.value = it
            }
        }
    }

    private fun refreshAds() {
        viewModelScope.launch {
            refreshRepository.refreshAds().collect {
                _refreshUiState.value = it
            }
        }
    }

    fun refreshPackages() {
        viewModelScope.launch {
            refreshRepository.refreshPackages().collect {
                _refreshUiState.value = it
            }
        }
    }

    fun refreshHome() {
        viewModelScope.launch {
            refreshSongs()
            refreshAds()
        }
    }
}


