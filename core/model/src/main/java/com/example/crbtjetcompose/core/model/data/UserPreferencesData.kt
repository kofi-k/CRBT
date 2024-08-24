package com.example.crbtjetcompose.core.model.data

data class UserPreferencesData(
    val userId: String,
    val phoneNumber: String,
    val languageCode: String,
    val profileUrl: String,
    val paymentMethod: String,
    val currency: String,
)
