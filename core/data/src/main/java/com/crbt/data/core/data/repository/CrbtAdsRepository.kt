package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.core.network.model.CrbtNetworkAds
import com.crbt.core.network.model.asExternalModel
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.itengs.crbt.core.model.data.CrbtAdResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
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
                .filter { dateString ->
                    val date = LocalDate.parse(dateString.expiryDate)
                    date.isAfter(LocalDate.now())
                }
            emit(CrbtAdsUiState.Success(ads))
        } catch (e: IOException) {
            when (e) {
                is ConnectException -> emit(CrbtAdsUiState.Error("Oops! your internet connection seem to be off."))
                is SocketTimeoutException -> emit(CrbtAdsUiState.Error("Hmm, connection timed out"))
                is UnknownHostException -> emit(CrbtAdsUiState.Error("A network error occurred. Please check your connection and try again."))
                else -> emit(CrbtAdsUiState.Error(e.message ?: "An error occurred"))
            }
        } catch (e: Exception) {
            emit(CrbtAdsUiState.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(ioDispatcher)

}