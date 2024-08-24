package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.datastore.CrbtPreferencesDataSource
import com.example.crbtjetcompose.core.model.data.UserPreferencesData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CrbtPreferencesRepositoryImpl @Inject constructor(
    private val crbtPreferencesDataSource: CrbtPreferencesDataSource
): CrbtPreferencesRepository {

    override val userPreferencesData: Flow<UserPreferencesData>
        get() = TODO()

    override suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserName(firstName: String, lastName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserPhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserProfilePictureUrl(profilePictureUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserLanguageCode(languageCode: String) {
        TODO("Not yet implemented")
    }
}