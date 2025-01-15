package com.itengs.crbt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.RefreshRepository
import com.crbt.data.core.data.repository.RefreshUiState
import com.itengs.crbt.core.model.data.UserPreferencesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val refreshRepository: RefreshRepository,
    userPreferencesRepository: CrbtPreferencesRepository
) : ViewModel() {
    private val _refreshUiState = MutableStateFlow<RefreshUiState>(RefreshUiState.Success)
    val refreshUiState: StateFlow<RefreshUiState> get() = _refreshUiState


    val uiState: StateFlow<MainActivityUiState> =
        userPreferencesRepository.userPreferencesData
            .map {
                MainActivityUiState.Success(it)
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = MainActivityUiState.Loading,
                started = SharingStarted.Eagerly,
            )

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


sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserPreferencesData) : MainActivityUiState
}