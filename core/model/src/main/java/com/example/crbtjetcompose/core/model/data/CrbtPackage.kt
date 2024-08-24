package com.example.crbtjetcompose.core.model.data

data class CrbtPackage(
    val id: String,
    val packageName: String,
    val packageDescription: String,
) {

    companion object {
        val dummyPackages = listOf(
            CrbtPackage(
                id = "1",
                packageName = "5G",
                packageDescription = "5G Description",
            ),
            CrbtPackage(
                id = "2",
                packageName = "Voice",
                packageDescription = "Voice Description",
            ),
            CrbtPackage(
                id = "3",
                packageName = "Internet",
                packageDescription = "Internet Description",
            ),
            CrbtPackage(
                id = "4",
                packageName = "Voice & Internet",
                packageDescription = "Voice & Internet Description",
            ),
            CrbtPackage(
                id = "5",
                packageName = "Flexi Package",
                packageDescription = "Flexi Package Description",
            ),
        )
    }
}
