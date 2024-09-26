package com.crbt.data.core.data.util

sealed class SnackbarMessageHandler<T> {
    data class Message<T>(val message: T) : SnackbarMessageHandler<T>()
}