package com.example.crbtjetcompose.core.network.repository

interface TokenProvider {
    suspend fun getToken(): String?
}
