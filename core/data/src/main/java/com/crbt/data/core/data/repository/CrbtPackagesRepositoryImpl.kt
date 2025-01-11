package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.common.core.common.result.Result
import com.crbt.core.network.model.asExternalModel
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

const val REGISTRATION_PACKAGE_NAME = "registration"

class CrbtPackagesRepositoryImpl @Inject constructor(
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val crbtNetworkRepository: CrbtNetworkRepository
) : CrbtPackagesRepository {

    private fun getAllPackages(): Flow<PackagesFeedUiState> = flow {
        emit(PackagesFeedUiState.Loading)
        try {
            val packageCategories = crbtNetworkRepository.getPackageCategories()
            val combinedPackagesAndItems = packageCategories
                .map { category ->
                    UserPackageResources(
                        category = category.asExternalModel(),
                        packageItems = category.packages.map { it.asExternalModel() }
                    )
                }
            emit(
                PackagesFeedUiState.Success(combinedPackagesAndItems)
            )
        } catch (e: IOException) {
            when (e) {
                is ConnectException -> emit(PackagesFeedUiState.Error("Oops! your internet connection seem to be off."))
                is SocketTimeoutException -> emit(PackagesFeedUiState.Error("Hmm, connection timed out"))
                is UnknownHostException -> emit(PackagesFeedUiState.Error("A network error occurred. Please check your connection and try again."))
                else -> emit(PackagesFeedUiState.Error(e.message ?: "An error occurred"))
            }
        } catch (e: Exception) {
            emit(PackagesFeedUiState.Error(e.message ?: "An error occurred"))
        }
    }
        .flowOn(ioDispatcher)

    override fun getEthioPackages(): Flow<PackagesFeedUiState> =
        getAllPackages()
            .map { packagesFeedUiState ->
                when (packagesFeedUiState) {
                    is PackagesFeedUiState.Success -> {
                        val ethioPackages = packagesFeedUiState.feed
                            .filter {
                                it.category.packageName.contains(
                                    REGISTRATION_PACKAGE_NAME,
                                    ignoreCase = true
                                ).not()
                            }
                        PackagesFeedUiState.Success(ethioPackages)
                    }

                    is PackagesFeedUiState.Error -> packagesFeedUiState
                    is PackagesFeedUiState.Loading -> packagesFeedUiState
                }
            }

    override fun getCRBTRegistrationPackages(): Flow<Result<UserPackageResources?>> =
        getAllPackages()
            .map { packagesFeedUiState ->
                when (packagesFeedUiState) {
                    is PackagesFeedUiState.Success -> {
                        val registrationPackages = packagesFeedUiState.feed
                            .find {
                                it.category.packageName.contains(
                                    REGISTRATION_PACKAGE_NAME,
                                    ignoreCase = true
                                )
                            }
                        Result.Success(registrationPackages)
                    }

                    is PackagesFeedUiState.Error -> Result.Error(Throwable(packagesFeedUiState.message))
                    is PackagesFeedUiState.Loading -> Result.Loading
                }
            }
}