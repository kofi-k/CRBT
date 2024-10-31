package com.crbt.data.core.data.di

import com.crbt.data.core.data.repository.CompositePackageResourcesRepository
import com.crbt.data.core.data.repository.CrbtPackageResourcesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface CrbtPackageResourcesRepositoryModule {
    @Binds
    fun bindsCrbtPackageResourcesRepository(
        compositePackageResourcesRepository: CompositePackageResourcesRepository,
    ): CrbtPackageResourcesRepository

}