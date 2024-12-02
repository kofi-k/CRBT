package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.crbt.data.core.data.repository.network.CrbtNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CrbtUserMonitor @Inject constructor(
    private val crbtPreferencesRepository: CrbtPreferencesRepository,
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val crbtNetworkRepository: CrbtNetworkRepository,
) : LoginManager {
    override val isLoggedIn: Flow<Boolean>
        get() = crbtPreferencesRepository.userPreferencesData.map { it.token.isNotBlank() }

    override suspend fun login(
        phone: String,
        accountType: String,
        langPref: String
    ) {
        val response = crbtNetworkRepository.login(
            phone = phone,
            accountType = accountType,
            langPref = langPref
        )
        with(response) {
            crbtPreferencesRepository.setSignInToken(token)
            crbtPreferencesRepository.setUserInfo(
                firstName = account.firstName ?: "",
                lastName = account.lastName ?: "",
                phoneNumber = phone,
                langPref = langPref,
                rewardPoints = account.rewardPoints ?: 0
            )
            crbtPreferencesRepository.updateCrbtSubscriptionId(account.subSongId)
        }
    }


    override suspend fun getAccountInfo() {
        val response = crbtNetworkRepository.getUserAccountInfo()
        with(response) {
            crbtPreferencesRepository.setUserInfo(
                firstName,
                lastName,
                phone,
                langPref,
                rewardPoints ?: 0
            )
        }
    }

    override suspend fun updateUserInfo(
        firstName: String,
        lastName: String
    ): Flow<UpdateUserInfoUiState> = flow {
        emit(UpdateUserInfoUiState.Loading)
        try {
            crbtNetworkRepository.updateUserAccountInfo(firstName, lastName)
            getAccountInfo()
            emit(UpdateUserInfoUiState.Success)
        } catch (e: Exception) {
            emit(UpdateUserInfoUiState.Error(e.message ?: "Error"))
        }
    }.flowOn(ioDispatcher)

}