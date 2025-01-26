package com.crbt

import com.itengs.crbt.core.model.data.LikeableToneCategory

sealed interface LikeableToneCategoriesUiState {
    data object Loading : LikeableToneCategoriesUiState
    data class Shown(
        val toneCategories: List<LikeableToneCategory>,
    )
}