package com.example.crbtjetcompose.core.model.data

data class PackageItem(
    val id: String,
    val packageCatId: String,
    val title: String,
    val description: String,
    val price: String,
    val packageValidity: String,
    val packageImg: String?,
    val ussdCode: String,
    val packageType: String,
) {

    fun itemValidity(): String {
        return when {
            packageValidity.contains("D", ignoreCase = true) -> {
                val days = packageValidity.replace("D", "", ignoreCase = true)
                "$days days"
            }

            packageValidity.contains("W", ignoreCase = true) -> {
                val weeks = packageValidity.replace("W", "", ignoreCase = true)
                "$weeks weeks"
            }

            packageValidity.contains("M", ignoreCase = true) -> {
                val months = packageValidity.replace("M", "", ignoreCase = true)
                "$months months"
            }

            packageValidity.contains("I", ignoreCase = true) -> {
                "Unlimited"
            }

            else -> {
                "Unknown"
            }
        }
    }

}
