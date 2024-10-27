package com.example.crbtjetcompose.core.network.model

import com.example.crbtjetcompose.core.model.data.PackageItem
import kotlinx.serialization.Serializable


@Serializable
data class NetworkPackageItem(
    val id: Int,
    val packageCatId: Int,
    val packageName: String,
    val description: String,
    val price: String,
    val packageValidity: String,
    val packageImg: String,
    val ussdCode: String,
    val packageType: String,
)

fun NetworkPackageItem.asExternalModel(): PackageItem {
    return PackageItem(
        id = id.toString(),
        packageCatId = packageCatId.toString(),
        title = packageName,
        description = description,
        price = price,
        packageValidity = packageValidity,
        packageImg = packageImg,
        ussdCode = ussdCode,
        packageType = packageType,
    )
}
