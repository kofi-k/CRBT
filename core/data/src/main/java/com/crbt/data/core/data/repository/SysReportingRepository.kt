package com.crbt.data.core.data.repository

import com.crbt.core.network.model.SysReportsNetworkModel
import com.crbt.core.network.retrofit.RetrofitCrbtNetworkApi
import java.io.IOException
import javax.inject.Inject

interface SysReportingRepository {
    suspend fun reportIssue(
        sysReportsNetworkModel: SysReportsNetworkModel
    ): SysReportingUiState
}


class SysReportingRepositoryImpl @Inject constructor(
    private val retrofitCrbtNetworkApi: RetrofitCrbtNetworkApi,
) : SysReportingRepository {
    override suspend fun reportIssue(
        sysReportsNetworkModel: SysReportsNetworkModel
    ): SysReportingUiState {
        return try {
            retrofitCrbtNetworkApi.reportIssue(sysReportsNetworkModel)
            SysReportingUiState.Success
        } catch (e: Exception) {
            when (e) {
                is IOException -> SysReportingUiState.Error("A network error occurred. Please try again later.")
                else -> SysReportingUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class SysReportingUiState {
    data object Loading : SysReportingUiState()
    data object Idle : SysReportingUiState()
    data object Success : SysReportingUiState()
    data class Error(val message: String) : SysReportingUiState()
}