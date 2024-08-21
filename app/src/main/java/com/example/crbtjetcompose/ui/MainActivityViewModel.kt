package com.example.crbtjetcompose.ui

import androidx.lifecycle.ViewModel
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    crbtPreferencesRepository: CrbtPreferencesRepository
)  : ViewModel() {

}