package com.crbt.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TonesViewModel @Inject constructor(
    private val userPreferencesRepository: CrbtPreferencesRepository
) : ViewModel() {

    fun updateCategorySelection(category: String, isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setUserInterestedToneCategories(category, isChecked)
        }
    }
}