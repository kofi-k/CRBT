package com.example.crbtjetcompose.core.model.data


/**
 *  an assumed user model based of the figma design,
 *  this would be updated to match the actual user model.
 *
 * will need a network model of this user model  in network module
 * and an entity model in the data module
 * */
data class CrbtUser(
    val userId: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val accountBalance: Double,
)


