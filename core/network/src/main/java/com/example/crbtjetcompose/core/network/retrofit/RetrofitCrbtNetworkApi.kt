package com.example.crbtjetcompose.core.network.retrofit

import com.example.crbtjetcompose.core.network.model.Login
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitCrbtNetworkApi {
    @GET("songs")
    suspend fun getSongs(): CrbtNetworkResponse

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
data class CrbtNetworkResponse(
    val allSongs: List<NetworkSongsResource>
)

@Serializable
data class NetworkPagination(
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val totalItems: Int
)


