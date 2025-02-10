package com.crbt.core.network.di

import androidx.tracing.trace
import com.crbt.core.network.model.ApiErrorResponse
import com.crbt.core.network.repository.TokenProvider
import com.crbt.core.network.repository.TokenProviderImpl
import com.crbt.core.network.retrofit.RetrofitCrbtNetworkApi
import com.itengs.crbt.core.network.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Singleton


private const val CRBT_BASE_URL = BuildConfig.BACKEND_URL
const val SYS_MAINTENANCE_CODE = 503

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenProvider: TokenProvider,
        json: Json
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val token = runBlocking { tokenProvider.getToken() }
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                val response = chain.proceed(newRequest)

                if (!response.isSuccessful) {
                    // Create a copy of response body before reading it
                    val responseBody = response.body
                    val errorBody = responseBody?.string()

                    // Create a new response with the consumed body for downstream consumers
                    val newResponse = response.newBuilder()
                        .body(errorBody?.toResponseBody(responseBody.contentType()))
                        .build()

                    val apiError = errorBody?.let {
                        try {
                            json.decodeFromString<ApiErrorResponse>(it)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    runBlocking {
                        tokenProvider.setSystemUnderMaintenance(response.code == SYS_MAINTENANCE_CODE)
                    }

                    val errorMessage = when (response.code) {
                        SYS_MAINTENANCE_CODE -> "System is under maintenance."
                        else -> apiError?.error
                            ?: "An unexpected error occurred. Code: ${response.code}"
                    }

                    throw HttpException(newResponse.code, errorMessage)
                }

                response
            }
            .build()
    }


    @Singleton
    @Provides
    fun provideTokenProvider(tokenProvider: TokenProviderImpl): TokenProvider = tokenProvider


    @Singleton
    @Provides
    fun provideFauApiService(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CRBT_BASE_URL)
            .addConverterFactory(
                providesNetworkJson()
                    .asConverterFactory("application/json".toMediaType())
            )
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


class HttpException(val statusCode: Int, message: String) : IOException(message)
