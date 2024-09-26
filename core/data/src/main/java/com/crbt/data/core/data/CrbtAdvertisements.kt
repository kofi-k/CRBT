package com.crbt.data.core.data

data class CrbtAdvertisements(
    val id: String,
    val title: String,
    val imageUrl: String,
    val link: String,
) {
    companion object {
        val ads = listOf(
            CrbtAdvertisements(
                id = "1",
                title = "Get 50% off on your next purchase",
                imageUrl = "https://images.unsplash.com/photo-1649972904349-6e44c42644a7?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxzZWFyY2h8MXx8dGVjaG5vbG9neXxlbnwwfHwwfHx8MA%3D%3D",
                link = ""
            ),

            CrbtAdvertisements(
                id = "2",
                title = "Enjoy unlimited music streaming",
                imageUrl = "https://images.unsplash.com/photo-1535223289827-42f1e9919769?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzl8fHRlY2hub2xvZ3l8ZW58MHx8MHx8fDA%3D",
                link = ""
            ),
            CrbtAdvertisements(
                id = "3",
                title = "Stay connected always with all that matters",
                imageUrl = "https://images.unsplash.com/photo-1581092795360-fd1ca04f0952?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjR8fHRlY2hub2xvZ3l8ZW58MHx8MHx8fDA%3D",
                link = "https://www.google.com"
            ),
            CrbtAdvertisements(
                id = "4",
                title = "Get 50% bonus on your next recharge",
                imageUrl = "https://images.unsplash.com/photo-1483058712412-4245e9b90334?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjZ8fHRlY2hub2xvZ3l8ZW58MHx8MHx8fDA%3D",
                link = ""
            ),
        )
    }
}