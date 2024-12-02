package com.example.crbtjetcompose.core.model.data

import androidx.compose.runtime.Immutable


/**
 *  A [CrbtSongResource] with additional details such as whether the user
 *  has subscribed to the song or has gifted the song.
 * */

@Immutable
class UserCRbtSongResource internal constructor(
    val id: String,
    val songTitle: String,
    val artisteName: String,
    val tune: String,
    val profile: String,
    val subServiceId: String,
    val albumName: String,
    val lang: String,
    val date: String,
    val numberOfListeners: Int,
    val numberOfSubscribers: Int,
    val ussdCode: String,
    val subscriptionType: String,
    val price: String,
    val category: String,
    val isSubscribed: Boolean,
    val hasGiftedSong: Boolean,
    val registrationUssdCode: String
) {
    constructor(
        userPreferencesData: UserPreferencesData,
        crbtSongResource: CrbtSongResource,
    ) : this(
        id = crbtSongResource.id,
        songTitle = crbtSongResource.songTitle,
        artisteName = crbtSongResource.artisteName,
        tune = crbtSongResource.tune,
        profile = crbtSongResource.profile,
        subServiceId = crbtSongResource.subServiceId,
        albumName = crbtSongResource.albumName,
        lang = crbtSongResource.lang,
        date = crbtSongResource.createdAt,
        numberOfListeners = crbtSongResource.numberOfListeners,
        numberOfSubscribers = crbtSongResource.numberOfSubscribers,
        ussdCode = crbtSongResource.ussdCode,
        subscriptionType = crbtSongResource.subscriptionType,
        price = crbtSongResource.price,
        category = crbtSongResource.category,
        isSubscribed = crbtSongResource.id == userPreferencesData.currentCrbtSubscriptionId.toString(),
        hasGiftedSong = crbtSongResource.id in userPreferencesData.giftedCrbtToneIds,
        registrationUssdCode = crbtSongResource.registrationUssdCode
    )
}


fun List<CrbtSongResource>.mapToUserCrbtSongResource(
    userPreferencesData: UserPreferencesData,
): List<UserCRbtSongResource> = map {
    it.asUserCrbtSongResource(userPreferencesData)
}


fun CrbtSongResource.asUserCrbtSongResource(
    userPreferencesData: UserPreferencesData,
): UserCRbtSongResource = UserCRbtSongResource(userPreferencesData, this)

fun UserCRbtSongResource.asCrbtSongResource(): CrbtSongResource = CrbtSongResource(
    id = id,
    songTitle = songTitle,
    artisteName = artisteName,
    tune = tune,
    profile = profile,
    subServiceId = subServiceId,
    albumName = albumName,
    lang = lang,
    createdAt = date,
    numberOfListeners = numberOfListeners,
    numberOfSubscribers = numberOfSubscribers,
    ussdCode = ussdCode,
    subscriptionType = subscriptionType,
    price = price,
    category = category,
    registrationUssdCode = registrationUssdCode
)
