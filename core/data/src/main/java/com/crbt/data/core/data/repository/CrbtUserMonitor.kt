package com.crbt.data.core.data.repository

import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.example.crbtjetcompose.core.network.repository.CrbtNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override suspend fun isDifferentUser(phoneNumber: String): Boolean {
        return crbtPreferencesRepository.userPreferencesData
            .map {
                when (isCurrentPhoneNumberEmpty()) {
                    true -> {
                        crbtPreferencesRepository.setPhoneNumber(phoneNumber)
                        return@map true
                    }

                    false -> {
                        val isDifferentUser = it.phoneNumber != phoneNumber
                        if (isDifferentUser) {
                            crbtPreferencesRepository.clearUserPreferences()
                            crbtPreferencesRepository.setPhoneNumber(phoneNumber)
                        }
                        isDifferentUser
                    }
                }
            }
            .first()
    }


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
        crbtPreferencesRepository.setSignInToken(response.token)
        getAccountInfo()
    }


    override suspend fun getAccountInfo() {
        val response = crbtNetworkRepository.getUserAccountInfo()
        with(response) {
            crbtPreferencesRepository.setUserInfo(
                firstName,
                lastName,
                phone,
                langPref
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

    private suspend fun isCurrentPhoneNumberEmpty(): Boolean {
        return crbtPreferencesRepository.userPreferencesData
            .map { it.phoneNumber.isBlank() }
            .first()
    }

}