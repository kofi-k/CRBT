package com.example.crbtjetcompose.core.network.di

import com.example.crbtjetcompose.core.network.repository.CrbtNetworkRepository
import com.example.crbtjetcompose.core.network.repository.CrbtNetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Singleton
    @Provides
    fun provideCrbtNetworkRepository(
        crbtNetworkRepositoryImpl: CrbtNetworkRepositoryImpl
    ): CrbtNetworkRepository {
        return crbtNetworkRepositoryImpl
    }

}