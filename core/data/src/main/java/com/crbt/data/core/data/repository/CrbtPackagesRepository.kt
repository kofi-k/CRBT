package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.CrbtPackageCategory
import com.example.crbtjetcompose.core.model.data.PackageItem
import kotlinx.coroutines.flow.Flow

interface CrbtPackagesRepository {
    fun getPackageCategories(): Flow<CrbtPackageState>

    fun getPackageItems(): Flow<CrbtPackageItemState>

}


sealed class CrbtPackageState {
    data object Loading : CrbtPackageState()
    data class Success(val crbtPackageCategories: List<CrbtPackageCategory>) : CrbtPackageState()
    data class Error(val message: String) : CrbtPackageState()
}

sealed class CrbtPackageItemState {
    data object Loading : CrbtPackageItemState()
    data class Success(val items: List<PackageItem>) : CrbtPackageItemState()
    data class Error(val message: String) : CrbtPackageItemState()
}