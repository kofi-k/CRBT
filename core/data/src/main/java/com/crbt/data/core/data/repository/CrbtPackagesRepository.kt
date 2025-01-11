package com.crbt.data.core.data.repository

import com.crbt.common.core.common.result.Result
import com.itengs.crbt.core.model.data.CrbtPackageCategory
import com.itengs.crbt.core.model.data.PackageItem
import kotlinx.coroutines.flow.Flow

interface CrbtPackagesRepository {
    fun getEthioPackages(): Flow<PackagesFeedUiState>
    fun getCRBTRegistrationPackages(): Flow<Result<UserPackageResources?>>
}

data class UserPackageResources(
    val category: CrbtPackageCategory,
    val packageItems: List<PackageItem>
)

sealed interface PackagesFeedUiState {
    data object Loading : PackagesFeedUiState
    data class Success(val feed: List<UserPackageResources>) : PackagesFeedUiState
    data class Error(val message: String) : PackagesFeedUiState
}


fun UserPackageResources.findPackageDurationItemById(packageId: String): String {
    return packageItems.find { it.id == packageId }?.itemValidity() ?: "unset"
}