package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.core.network.model.NetworkSongsResource
import com.crbt.core.network.model.asExternalModel
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
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
            } catch (e: IOException) {
                when (e) {
                    is ConnectException -> emit(CrbtMusicResourceUiState.Error("Oops! your internet connection seem to be off."))
                    is SocketTimeoutException -> emit(CrbtMusicResourceUiState.Error("Hmm, connection timed out."))
                    is UnknownHostException -> emit(CrbtMusicResourceUiState.Error("An error occurred while connecting to the server. Please try again later."))
                    else -> emit(
                        CrbtMusicResourceUiState.Error(
                            e.message ?: "A network error occurred. Please try again later"
                        )
                    )
                }
            } catch (e: Exception) {
                emit(CrbtMusicResourceUiState.Error(e.message ?: "An error occurred"))
            }
        }.flowOn(ioDispatcher)
}