package com.example.crbtjetcompose.core.model.data


/**
 *  an assumed user model based of the figma design,
 *  this would be updated to match the actual user model
 *
 * */
data class CrbtUser(
    val userId: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accountBalance: Double,
)


