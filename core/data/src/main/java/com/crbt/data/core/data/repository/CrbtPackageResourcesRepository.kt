package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.CrbtPackageCategory
import com.example.crbtjetcompose.core.model.data.PackageItem
import kotlinx.coroutines.flow.Flow

interface CrbtPackageResourcesRepository {
    fun observeCrbtPackageResources(): Flow<PackagesFeedUiState>
}


data class UserPackageResources(
    val categories: CrbtPackageCategory,
    val packageItems: List<PackageItem>
)

sealed interface PackagesFeedUiState {
    data object Loading : PackagesFeedUiState
    data class Success(val feed: List<UserPackageResources>) : PackagesFeedUiState
    data class Error(val message: String) : PackagesFeedUiState
}