package com.crbt.data.core.data.model

import com.example.crbtjetcompose.core.model.data.CrbtUser


fun CrbtUser.asEntity() = CrbtUser(
    userId = userId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    email = email,
    accountBalance = accountBalance,
    profileUrl = profileUrl,
)

// dummy user object
data object DummyUser {
    val user =
        CrbtUser(
            email = "sampleuser@gmail.com",
            userId = "",
            lastName = "Kebede",
            firstName = "Kebede T.",
            phoneNumber = "251 523 9678",
            accountBalance = 100.0,
            profileUrl = ""
        )
}

fun CrbtUser.fullName(): String {
    return "$firstName $lastName"
}