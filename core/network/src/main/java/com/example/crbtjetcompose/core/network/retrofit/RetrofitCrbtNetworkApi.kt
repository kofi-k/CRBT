package com.example.crbtjetcompose.core.network.retrofit

import com.example.crbtjetcompose.core.network.model.CrbtNetworkAds
import com.example.crbtjetcompose.core.network.model.CrbtNetworkPackage
import com.example.crbtjetcompose.core.network.model.Login
import com.example.crbtjetcompose.core.network.model.LoginResponse
import com.example.crbtjetcompose.core.network.model.NetworkPackageItem
import com.example.crbtjetcompose.core.network.model.NetworkSongsResource
import com.example.crbtjetcompose.core.network.model.NetworkUserAccountInfo
import com.example.crbtjetcompose.core.network.model.SubscriptionRequest
import com.example.crbtjetcompose.core.network.model.SubscriptionResponse
import com.example.crbtjetcompose.core.network.model.UpdateUserInfo
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface RetrofitCrbtNetworkApi {
    @GET("songs")
    suspend fun getSongs(): CrbtNetworkResponse

    @GET("package/all")
    suspend fun getPackageItems(): List<NetworkPackageItem>

    @GET("package/all/category")
    suspend fun getPackageCategories(): List<CrbtNetworkPackage>

    @GET("ads/all")
    suspend fun getAds(): List<CrbtNetworkAds>

    @POST("songs/subscribe/{songId}")
    suspend fun subscribeToCrbt(
        @Path("songId") songId: Int,
    ): SubscriptionResponse

    @POST("service/unsubscribe")
    suspend fun unsubscribe(
        @Body subscriptionRequest: SubscriptionRequest
    )

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: Login
    ): LoginResponse

    @GET("user/account-info")
    suspend fun getUserAccountInfo(): NetworkUserAccountInfo

    @PUT("user/update-account-info")
    suspend fun updateUserAccountInfo(
        @Body updateUserInfo: UpdateUserInfo
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


