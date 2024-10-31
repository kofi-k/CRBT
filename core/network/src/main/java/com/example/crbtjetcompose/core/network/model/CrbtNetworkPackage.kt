package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.CrbtPackageCategory
import kotlinx.serialization.Serializable


@Serializable
data class CrbtNetworkPackage(
    val id: Int,
    val title: String,
    val description: String,
)


fun CrbtNetworkPackage.asExternalModel(): CrbtPackageCategory {
    return CrbtPackageCategory(
        id = id.toString(),
        packageName = title,
        packageDescription = description,
    )
}