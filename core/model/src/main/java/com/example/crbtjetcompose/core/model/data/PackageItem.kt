package com.example.crbtjetcompose.core.model.data

data class PackageItem(
    val id: String,
    val packageCatId: String,
    val title: String,
    val description: String,
    val price: String,
    val packageValidity: String,
    val packageImg: String,
    val ussdCode: String,
    val packageType: String,
) {
    companion object {
        val dummyPackages = listOf(
            PackageItem(
                id = "1",
                packageCatId = "1",
                title = "5G Package",
                description = "5G Description",
                price = "1000",
                packageValidity = "1 day",
                packageImg = "5G",
                ussdCode = "*123*1#",
                packageType = "5G MetaData",
            ),
            PackageItem(
                id = "2",
                packageCatId = "2",
                title = "Voice",
                description = "Voice Description",
                price = "500",
                packageValidity = "1 week",
                packageImg = "Voice",
                ussdCode = "*123*2#",
                packageType = "Voice MetaData",
            ),
            PackageItem(
                id = "3",
                packageCatId = "3",
                title = "Internet",
                description = "Internet Description",
                price = "2000",
                packageValidity = "1 month",
                packageImg = "Internet",
                ussdCode = "*123*3#",
                packageType = "Internet MetaData",
            ),
            PackageItem(
                id = "4",
                packageCatId = "4",
                title = "Voice & Internet",
                description = "Voice & Internet Description",
                price = "3000",
                packageValidity = "3 months",
                packageImg = "",
                ussdCode = "*123*4#",
                packageType = "Voice & Internet MetaData",
            ),
            PackageItem(
                id = "5",
                packageCatId = "5",
                title = "Flexi Package",
                description = "Flexi Package Description",
                price = "1500",
                packageValidity = "Unlimited",
                packageImg = "Flexi Package",
                ussdCode = "*123*5#",
                packageType = "Flexi Package MetaData",
            ),

            PackageItem(
                id = "6",
                packageCatId = "1",
                title = "5G Package",
                description = "5G Description",
                price = "1000",
                packageValidity = "1 day",
                packageImg = "5G",
                ussdCode = "*123*1#",
                packageType = "5G MetaData",
            ),
            PackageItem(
                id = "7",
                packageCatId = "1",
                title = "Voice",
                description = "Voice Description",
                price = "500",
                packageValidity = "1 week",
                packageImg = "Voice",
                ussdCode = "*123*2#",
                packageType = "Voice MetaData",
            ),
            PackageItem(
                id = "8",
                packageCatId = "2",
                title = "Internet",
                description = "Internet Description",
                price = "2000",
                packageValidity = "1 month",
                packageImg = "Internet",
                ussdCode = "*123*3#",
                packageType = "Internet MetaData",
            ),
            PackageItem(
                id = "9",
                packageCatId = "4",
                title = "Voice & Internet",
                description = "Voice & Internet Description",
                price = "3000",
                packageValidity = "3 months",
                packageImg = "Voice & Internet",
                ussdCode = "*123*4#",
                packageType = "Voice & Internet MetaData",
            ),
            PackageItem(
                id = "10",
                packageCatId = "2",
                title = "Flexi Package",
                description = "Flexi Package Description",
                price = "1500",
                packageValidity = "Unlimited",
                packageImg = "Flexi Package",
                ussdCode = "*123*5#",
                packageType = "Flexi Package MetaData",
            ),
        )
    }

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
