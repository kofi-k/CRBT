package com.example.crbtjetcompose.core.network.retrofit

import com.example.crbtjetcompose.core.network.model.Login
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RetrofitCrbtNetworkApi {
    @GET("songs/all")
    suspend fun getSongs(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): NetworkResponse<NetworkSongsResource>

    @POST("service/subscribe")
    suspend fun subscribe(
        @Body subscriptionRequest: SubscriptionRequest
    )

    @POST("service/unsubscribe")
    suspend fun unsubscribe(
        @Body subscriptionRequest: SubscriptionRequest
    )

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: Login
    )

}


@Serializable
data class NetworkResponse<T>(
    val results: List<T>,
    val networkPagination: NetworkPagination
)

@Serializable
data class NetworkPagination(
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val totalItems: Int
)


