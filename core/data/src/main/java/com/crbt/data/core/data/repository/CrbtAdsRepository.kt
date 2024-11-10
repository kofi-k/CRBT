package com.crbt.data.core.data.repository

import android.util.Log
import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.example.crbtjetcompose.core.model.data.CrbtAdResource
import com.example.crbtjetcompose.core.network.model.CrbtNetworkAds
import com.example.crbtjetcompose.core.network.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


interface CrbtAdsRepository {
    fun getAds(): Flow<CrbtAdsUiState>
}

sealed class CrbtAdsUiState {
    data object Loading : CrbtAdsUiState()
    data class Success(val data: List<CrbtAdResource>) : CrbtAdsUiState()
    data class Error(val message: String) : CrbtAdsUiState()
}

class CrbtAdsRepositoryImpl @Inject constructor(
    private val crbtNetworkRepository: CrbtNetworkRepository,
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CrbtAdsRepository {
    override fun getAds(): Flow<CrbtAdsUiState> = flow {
        emit(CrbtAdsUiState.Loading)
        try {
            val ads = crbtNetworkRepository.getAds()
                .map(CrbtNetworkAds::asExternalModel)
                .sortedByDescending { it.id }
            emit(CrbtAdsUiState.Success(ads))
        } catch (e: Exception) {
            Log.e("CrbtAdsRepositoryImpl", "getAds: $e")
            when (e) {
                is SocketTimeoutException, is UnknownHostException,
                is ConnectException -> emit(CrbtAdsUiState.Error("Network Error. Try again later"))

                else -> emit(CrbtAdsUiState.Error(e.message ?: "An error occurred"))
            }
        }
    }.flowOn(ioDispatcher)

}