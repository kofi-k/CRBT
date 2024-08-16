package com.crbt.data.core.data.model

data class CrbtToneGenres(
    val id: String,
    val name: String,
    val description: String
)


//RnB, HipHop, Rock, Country, Pop, Afrobeat
data object SampleToneGenres {
    val genres = listOf(
        CrbtToneGenres(
            id = "1",
            name = "Afrobeat",
            description = "RnB tones"
        ),
        CrbtToneGenres(
            id = "2",
            name = "HipHop",
            description = " HipHop"
        ),
        CrbtToneGenres(
            id = "3",
            name = "Rock",
            description = "Rock tones"
        ),
        CrbtToneGenres(
            id = "4",
            name = "Country",
            description = "Country tones"
        ),
        CrbtToneGenres(
            id = "5",
            name = "Pop",
            description = "Pop tones"
        ),
        CrbtToneGenres(
            id = "1",
            name = "RnB",
            description = "Afrobeat tones"
        ),
    )
}