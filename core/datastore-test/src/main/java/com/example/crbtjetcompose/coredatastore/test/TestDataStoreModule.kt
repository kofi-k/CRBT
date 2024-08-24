package com.example.crbtjetcompose.coredatastore.test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.crbt.common.core.common.network.di.ApplicationScope
import com.example.crbtjetcompose.core.datastore.UserPreferences
import com.example.crbtjetcompose.core.datastore.UserPreferencesSerializer
import com.example.crbtjetcompose.core.datastore.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DataStoreModule::class],
//)
//internal object TestDataStoreModule {
//
//    @Provides
//    @Singleton
//    fun providesUserPreferencesDataStore(
//        @ApplicationScope scope: CoroutineScope,
//        userPreferencesSerializer: UserPreferencesSerializer,
//        tmpFolder: TemporaryFolder,
//    ): DataStore<UserPreferences> =
//        tmpFolder.testUserPreferencesDataStore(
//            coroutineScope = scope,
//            userPreferencesSerializer = userPreferencesSerializer,
//        )
//}
//
//fun TemporaryFolder.testUserPreferencesDataStore(
//    coroutineScope: CoroutineScope,
//    userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer(),
//) = DataStoreFactory.create(
//    serializer = userPreferencesSerializer,
//    scope = coroutineScope,
//) {
//    newFile("user_preferences_test.pb")
//}
