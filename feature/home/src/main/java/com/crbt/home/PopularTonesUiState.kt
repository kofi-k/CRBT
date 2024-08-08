package com.crbt.home

import com.example.crbtjetcompose.core.model.data.Tones


/**
 *  A sealed hierarchy describing the possible states of the popular tones for the day
 *  for the home screen
 * */
sealed interface PopularTonesUiState {
    /**
     * A state indicating that the popular tones are loading
     * */
    data object Loading : PopularTonesUiState

    /**
     * A state indicating that the popular tones failed to load
     * */
    data object LoadingFailed : PopularTonesUiState

    /**
     * A state indicating that the are no popular tones to show
     * */
    data object NotShown : PopularTonesUiState

    /**
     * A state indicating that there are popular tones to show given the list of tones
     * */
    data class Shown(val tones: List<Tones>) : PopularTonesUiState
}