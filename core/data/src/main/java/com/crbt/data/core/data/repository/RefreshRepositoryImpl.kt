package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.network.di.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RefreshRepositoryImpl @Inject constructor(
    private val loginManager: LoginManager,
    private val crbtMusicRepository: CrbtMusicRepository,
    private val crbtAdsRepository: CrbtAdsRepository,
    private val crbtPackagesRepository: CrbtPackagesRepository,
) : RefreshRepository {

    override suspend fun refreshUserInfo(): Flow<RefreshUiState> = flow {
        emit(RefreshUiState.Loading)
        try {
            loginManager.getAccountInfo()
            emit(RefreshUiState.Success)
        } catch (e: IOException) {
            when (e) {
                is ConnectException -> emit(RefreshUiState.Error("Oops! your internet connection seem to be off."))
                is SocketTimeoutException -> emit(RefreshUiState.Error("Hmm, connection timed out"))
                is UnknownHostException -> emit(RefreshUiState.Error("A network error occurred. Please check your connection and try again."))
                else -> emit(RefreshUiState.Error(e.message ?: "An error occurred"))
            }
        } catch (e: Exception) {
            emit(RefreshUiState.Error(e.message ?: "An error occurred"))
        } catch (e: HttpException) {
            emit(RefreshUiState.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun refreshSongs(): Flow<RefreshUiState> =
        crbtMusicRepository.getCrbtMusic().map {
            when (it) {
                is CrbtMusicResourceUiState.Success -> RefreshUiState.Success
                is CrbtMusicResourceUiState.Error -> RefreshUiState.Error(it.message)
                CrbtMusicResourceUiState.Loading -> RefreshUiState.Loading
            }
        }


    override suspend fun refreshAds(): Flow<RefreshUiState> =
        crbtAdsRepository.getAds().map {
            when (it) {
                is CrbtAdsUiState.Success -> RefreshUiState.Success
                is CrbtAdsUiState.Error -> RefreshUiState.Error(it.message)
                CrbtAdsUiState.Loading -> RefreshUiState.Loading
            }
        }

    override suspend fun refreshPackages(): Flow<RefreshUiState> =
        crbtPackagesRepository.getEthioPackages().map {
            when (it) {
                is PackagesFeedUiState.Success -> RefreshUiState.Success
                is PackagesFeedUiState.Error -> RefreshUiState.Error(it.message)
                PackagesFeedUiState.Loading -> RefreshUiState.Loading
            }
        }
}