package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import com.example.crbtjetcompose.core.network.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CrbtPackagesRepositoryImpl @Inject constructor(
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val crbtNetworkRepository: CrbtNetworkRepository
) : CrbtPackagesRepository {

    override fun getEthioPackages(): Flow<PackagesFeedUiState> = flow {
        emit(PackagesFeedUiState.Loading)
        try {
            val packageCategories = crbtNetworkRepository.getPackageCategories()
            val combinedPackagesAndItems = packageCategories.map { category ->
                UserPackageResources(
                    category = category.asExternalModel(),
                    packageItems = category.packages.map { it.asExternalModel() }
                )
            }
            emit(
                PackagesFeedUiState.Success(combinedPackagesAndItems)
            )
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException, is UnknownHostException,
                is ConnectException -> emit(PackagesFeedUiState.Error("Network Error. Try again later"))

                else ->
                    emit(PackagesFeedUiState.Error(e.message ?: "An error occurred"))
            }
        }
    }
        .flowOn(ioDispatcher)
}