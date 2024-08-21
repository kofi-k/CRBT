package com.crbt.data.core.data.di

import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepositoryImpl
import com.crbt.data.core.data.repository.CrbtMusicRepository
import com.crbt.data.core.data.repository.CrbtMusicRepositoryImpl
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.CrbtPreferencesRepositoryImpl
import com.crbt.data.core.data.repository.MusicRepository
import com.crbt.data.core.data.repository.MusicRepositoryImpl
import com.crbt.data.core.data.util.ConnectivityManagerNetworkMonitor
import com.crbt.data.core.data.util.NetworkMonitor
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

    @Binds
    abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    abstract fun bindsPhoneAuthRepository(
        phoneAuthRepositoryImpl: PhoneAuthRepositoryImpl
    ): PhoneAuthRepository

    @Binds
    abstract fun bindsCrbtPreferencesRepository(
        crbtPreferencesRepositoryImpl: CrbtPreferencesRepositoryImpl
    ): CrbtPreferencesRepository

    @Binds
    abstract fun bindsMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl
    ): MusicRepository
}