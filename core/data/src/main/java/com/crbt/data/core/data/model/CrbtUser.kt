package com.crbt.data.core.data.model

import com.example.crbtjetcompose.core.model.data.CrbtUser


// dummy user object
data object DummyUser {
    val user =
        CrbtUser(
            email = "sampleuser@gmail.com",
            userId = "",
            lastName = "Kebede",
            firstName = "Kebede T.",
            phoneNumber = "251 523 9678",
            accountBalance = 0.0,
            profileUrl = ""
        )
}

