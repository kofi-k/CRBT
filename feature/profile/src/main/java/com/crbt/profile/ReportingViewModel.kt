package com.crbt.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crbt.core.network.model.SysReportsNetworkModel
import com.crbt.data.core.data.repository.SysReportingRepository
import com.crbt.data.core.data.repository.SysReportingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportingViewModel @Inject constructor(
    private val sysReportingRepository: SysReportingRepository
) : ViewModel() {

    var reportingState by mutableStateOf<SysReportingUiState>(SysReportingUiState.Idle)
        private set

    fun submitReport(
        title: String,
        category: String,
        description: String,
        onSuccess: () -> Unit,
        onFail: (message: String) -> Unit,
    ) {
        reportingState = SysReportingUiState.Loading
        viewModelScope.launch {
            when (val state = sysReportingRepository.reportIssue(
                SysReportsNetworkModel(
                    title, category, description
                )
            )) {
                is SysReportingUiState.Success -> onSuccess()
                is SysReportingUiState.Error -> {
                    reportingState = state
                    onFail(state.message)
                }

                else -> reportingState = state
            }
        }
    }

}