package com.crbt.data.core.data.di

import com.crbt.data.core.data.musicService.MusicController
import com.crbt.data.core.data.musicService.MusicControllerImpl
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepository
import com.crbt.data.core.data.phoneAuth.PhoneAuthRepositoryImpl
import com.crbt.data.core.data.repository.CompositeUserCrbtSongsRepository
import com.crbt.data.core.data.repository.CrbtMusicRepository
import com.crbt.data.core.data.repository.CrbtMusicRepositoryImpl
import com.crbt.data.core.data.repository.CrbtPackagesRepository
import com.crbt.data.core.data.repository.CrbtPackagesRepositoryImpl
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.CrbtPreferencesRepositoryImpl
import com.crbt.data.core.data.repository.CrbtUserMonitor
import com.crbt.data.core.data.repository.LoginManager
import com.crbt.data.core.data.repository.UserCrbtMusicRepository
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.crbt.data.core.data.repository.network.CrbtNetworkRepositoryImpl
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
    abstract fun bindsCrbtNetworkRepository(
        crbtNetworkRepositoryImpl: CrbtNetworkRepositoryImpl
    ): CrbtNetworkRepository

    @Binds
    abstract fun bindsPhoneAuthRepository(
        phoneAuthRepositoryImpl: PhoneAuthRepositoryImpl
    ): PhoneAuthRepository

    @Binds
    abstract fun bindsCrbtPreferencesRepository(
        crbtPreferencesRepositoryImpl: CrbtPreferencesRepositoryImpl
    ): CrbtPreferencesRepository

    @Binds
    abstract fun bindsCrbtUserMonitor(
        crbtUserMonitor: CrbtUserMonitor
    ): LoginManager

    @Binds
    abstract fun bindsUserCrbtMusicRepository(
        compositeUserCrbtSongsRepository: CompositeUserCrbtSongsRepository
    ): UserCrbtMusicRepository

    @Binds
    abstract fun bindsMusicController(
        musicControllerImpl: MusicControllerImpl
    ): MusicController

    @Binds
    abstract fun bindsCrbtPackagesRepository(
        crbtPackageRepositoryImpl: CrbtPackagesRepositoryImpl
    ): CrbtPackagesRepository
}