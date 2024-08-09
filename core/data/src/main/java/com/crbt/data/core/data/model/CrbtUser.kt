package com.crbt.data.core.data.model

import com.example.crbtjetcompose.core.model.data.CrbtUser


fun CrbtUser.asEntity() = CrbtUser(
    userId = userId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    email = email,
    accountBalance = accountBalance,
)

// dummy user object
data object DummyUser {
    val user =
        CrbtUser(
            email = "sampleuser@gmail.com",
            userId = "dkfwo23902",
            lastName = "Kebede",
            firstName = "Kebede T.",
            phoneNumber = "0912345678",
            accountBalance = 100.0
        )
}