package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.CrbtPackageCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CrbtNetworkPackage(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("Packages")
    val packages: List<NetworkPackageItem>
)


fun CrbtNetworkPackage.asExternalModel(): CrbtPackageCategory {
    return CrbtPackageCategory(
        id = id.toString(),
        packageName = title,
        packageDescription = description,
    )
}