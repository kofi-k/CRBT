package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.PackageItem
import kotlinx.serialization.Serializable


@Serializable
data class NetworkPackageItem(
    val id: String,
    val packageId: String,
    val title: String,
    val description: String,
    val price: String,
    val validity: String,
    val packageImage: String,
    val ussdCode: String,
    val metaData: String,
)

fun NetworkPackageItem.asExternalModel(): PackageItem {
    return PackageItem(
        id = id,
        packageId = packageId,
        title = title,
        description = description,
        price = price,
        validity = validity,
        packageImage = packageImage,
        ussdCode = ussdCode,
        metaData = metaData,
    )
}
