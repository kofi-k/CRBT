package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.example.crbtjetcompose.core.network.model.asExternalModel
import com.example.crbtjetcompose.core.network.repository.CrbtNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CrbtPackagesRepositoryImpl @Inject constructor(
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val crbtNetworkRepository: CrbtNetworkRepository
) : CrbtPackagesRepository {
    override fun getPackageCategories(): Flow<CrbtPackageState> =
        flow {
            emit(CrbtPackageState.Loading)
            try {
                val packageCategories = crbtNetworkRepository.getPackageCategories()
                emit(CrbtPackageState.Success(packageCategories.map { it.asExternalModel() }))
            } catch (e: Exception) {
                emit(CrbtPackageState.Error(e.message ?: "An error occurred"))
            }
        }
            .flowOn(ioDispatcher)

    override fun getPackageItems(): Flow<CrbtPackageItemState> = flow {
        emit(CrbtPackageItemState.Loading)
        try {
            val packageItems = crbtNetworkRepository.getPackageItems()
            emit(CrbtPackageItemState.Success(packageItems.map { it.asExternalModel() }))
        } catch (e: Exception) {
            emit(CrbtPackageItemState.Error(e.message ?: "An error occurred"))
        }
    }
        .flowOn(ioDispatcher)
}