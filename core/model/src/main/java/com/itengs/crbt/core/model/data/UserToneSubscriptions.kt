package com.itengs.crbt.core.model.data

/**
 *  A [UserToneSubscriptions] model class that represents the user tone subscriptions
 *  This is an anticipated model class that would be updated to match the actual user tone subscriptions model
 *  or we may use the user subscription data together with the tones to represent the user tone subscriptions
 * */
data class UserToneSubscriptions internal constructor(
    val toneId: String,
    val toneName: String,
    val artist: String,
    val toneUrl: String,
    val toneImageUrl: String,
    val dueDate: String,
) {
    // will have to replace [crbtUser] with the actual user subscription data
    constructor(crbtSongResource: CrbtSongResource, crbtUser: CrbtUser) : this(
        toneId = crbtSongResource.id,
        toneName = crbtSongResource.songTitle,
        artist = crbtSongResource.artisteName,
        toneUrl = crbtSongResource.tune,
        toneImageUrl = crbtSongResource.profile,
        dueDate = "Due in 2 days" // todo get this due date based from the user subscription data
    )
}

/**
 *  Maps a list of [CrbtSongResource] to a list of [UserToneSubscriptions] using the [crbtUser] data
 *  @param crbtUser the user data
 *  @return a list of [UserToneSubscriptions]
 * */
fun List<CrbtSongResource>.mapToUserToneSubscriptions(crbtUser: CrbtUser): List<UserToneSubscriptions> =
    map { UserToneSubscriptions(it, crbtUser) }
