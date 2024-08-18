package com.crbt.data.core.data.di

import com.crbt.data.core.data.repository.CrbtMusicRepository
import com.crbt.data.core.data.repository.CrbtMusicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsCrbtMusicRepository(
        crbtMusicRepositoryImpl: CrbtMusicRepositoryImpl
    ): CrbtMusicRepository

}