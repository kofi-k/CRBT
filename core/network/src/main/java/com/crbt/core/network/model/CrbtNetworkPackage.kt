package com.crbt.core.network.model

import com.itengs.crbt.core.model.data.CrbtPackageCategory
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