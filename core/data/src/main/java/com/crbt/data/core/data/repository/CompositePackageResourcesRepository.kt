package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.CrbtPackageCategory
import com.example.crbtjetcompose.core.model.data.PackageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CompositePackageResourcesRepository @Inject constructor(
    private val crbtPackagesRepository: CrbtPackagesRepository
) : CrbtPackageResourcesRepository {

    override fun observeCrbtPackageResources(): Flow<PackagesFeedUiState> =
        crbtPackagesRepository.getPackageCategories()
            .combine(crbtPackagesRepository.getPackageItems()) { categories, items ->
                when {
                    categories is CrbtPackageState.Loading || items is CrbtPackageItemState.Loading ->
                        PackagesFeedUiState.Loading

                    categories is CrbtPackageState.Error ->
                        PackagesFeedUiState.Error(categories.message)

                    items is CrbtPackageItemState.Error ->
                        PackagesFeedUiState.Error(items.message)

                    categories is CrbtPackageState.Success && items is CrbtPackageItemState.Success -> {
                        mapCategoriesWithItems(
                            categories.crbtPackageCategories, items.items
                        )
                    }

                    else -> PackagesFeedUiState.Loading
                }
            }

    private fun mapCategoriesWithItems(
        categories: List<CrbtPackageCategory>,
        items: List<PackageItem>
    ): PackagesFeedUiState.Success {
        return PackagesFeedUiState.Success(
            categories.map { category ->
                UserPackageResources(
                    categories = category,
                    packageItems = items.filter { it.packageCatId == category.id }
                )
            }
        )
    }
}
