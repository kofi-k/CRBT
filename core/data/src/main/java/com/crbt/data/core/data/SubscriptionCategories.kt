package com.crbt.data.core.data

import com.itengs.crbt.core.model.data.SubscriptionCategory


// popular, phone, entertainment, packages
data object SubscriptionCategories {
    val categories = listOf(
        SubscriptionCategory(
            id = "1",
            name = "Popular",
            description = "Popular tones"
        ),
        SubscriptionCategory(
            id = "2",
            name = "Phone",
            description = " Phone"
        ),
        SubscriptionCategory(
            id = "3",
            name = "Entertainment",
            description = "Entertainment tones"
        ),
        SubscriptionCategory(
            id = "4",
            name = "Packages",
            description = "Packages"
        ),
    )
}