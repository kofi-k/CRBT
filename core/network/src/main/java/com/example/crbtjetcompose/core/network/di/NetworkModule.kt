package com.example.crbtjetcompose.core.network.di

import android.content.Context
import androidx.tracing.trace
import com.example.crbtjetcompose.core.network.BuildConfig
import com.example.crbtjetcompose.core.network.retrofit.RetrofitCrbtNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


private const val CRBT_BASE_URL = BuildConfig.BACKEND_URL

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideFauApiService(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(providesNetworkJson().asConverterFactory("application/json".toMediaType()))
            .baseUrl(CRBT_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideFauApiDataSource(
        retrofit: Retrofit
    ): RetrofitCrbtNetworkApi {
        return retrofit.create(RetrofitCrbtNetworkApi::class.java)
    }


    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("CrbtOkHttpClient") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()
    }


}
