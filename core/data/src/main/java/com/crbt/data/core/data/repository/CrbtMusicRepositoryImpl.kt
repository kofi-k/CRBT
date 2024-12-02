package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CrbtMusicRepositoryImpl @Inject constructor(
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val crbtNetworkRepository: CrbtNetworkRepository,
) : CrbtMusicRepository {
    override fun getCrbtMusic(): Flow<CrbtMusicResourceUiState> =
        flow {
            emit(CrbtMusicResourceUiState.Loading)
            try {
                val songs = crbtNetworkRepository
                    .getSongs()
                    .map(NetworkSongsResource::asExternalModel)
                emit(CrbtMusicResourceUiState.Success(songs))
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException, is UnknownHostException,
                    is ConnectException -> emit(CrbtMusicResourceUiState.Error("Network Error. Try again later"))

                    else -> emit(CrbtMusicResourceUiState.Error(e.message ?: "An error occurred"))
                }
            }
        }.flowOn(ioDispatcher)
}