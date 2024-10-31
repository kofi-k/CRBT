package com.example.crbtjetcompose.core.model.data

data class CrbtPackageCategory(
    val id: String,
    val packageName: String,
    val packageDescription: String,
) {

    companion object {
        val dummyPackages = listOf(
            CrbtPackageCategory(
                id = "1",
                packageName = "5G",
                packageDescription = "5G Description",
            ),
            CrbtPackageCategory(
                id = "2",
                packageName = "Voice",
                packageDescription = "Voice Description",
            ),
            CrbtPackageCategory(
                id = "3",
                packageName = "Internet",
                packageDescription = "Internet Description",
            ),
            CrbtPackageCategory(
                id = "4",
                packageName = "Voice & Internet",
                packageDescription = "Voice & Internet Description",
            ),
            CrbtPackageCategory(
                id = "5",
                packageName = "Flexi Package",
                packageDescription = "Flexi Package Description",
            ),
        )
    }
}
