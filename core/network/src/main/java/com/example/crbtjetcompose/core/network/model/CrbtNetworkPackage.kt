package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.CrbtPackage
import kotlinx.serialization.Serializable


@Serializable
data class CrbtNetworkPackage(
    val id: String,
    val packageName: String,
    val packageDescription: String,
)


fun CrbtNetworkPackage.asExternalModel(): CrbtPackage {
    return CrbtPackage(
        id = id,
        packageName = packageName,
        packageDescription = packageDescription,
    )
}