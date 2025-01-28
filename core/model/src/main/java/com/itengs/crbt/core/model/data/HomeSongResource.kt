package com.itengs.crbt.core.model.data

data class HomeSongResource(
    val popularTodaySongs: List<CrbtSongResource>,
    val currentUserCrbtSubscription: CrbtSongResource?
)