package com.example.crbtjetcompose.core.model.data

data class UserPreferencesData(
    val userId: String,
    val isUserSignedIn: Boolean,
    val phoneNumber: String,
    val languageCode: String,
    val profileUrl: String,
    val paymentMethod: String,
    val currency: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val currentBalance: Double,
    val interestedCrbtLanguages: Set<String>,
    val currentCrbtSubscriptionId: String,
    val giftedCrbtToneIds: Set<String>,
)
