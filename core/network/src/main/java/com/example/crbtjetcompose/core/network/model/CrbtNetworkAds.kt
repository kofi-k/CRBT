package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.CrbtAdResource
import kotlinx.serialization.Serializable


@Serializable
data class CrbtNetworkAds(
    val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val image: String,
    val expiryDate: String
)


fun CrbtNetworkAds.asExternalModel(): CrbtAdResource {
    return CrbtAdResource(
        id = id,
        title = title,
        description = description,
        url = url,
        image = image,
        expiryDate = expiryDate
    )
}
