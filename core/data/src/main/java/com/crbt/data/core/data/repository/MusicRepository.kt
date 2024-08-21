package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.model.data.MusicResource

interface MusicRepository {
    suspend fun getMusicFiles(): List<MusicResource>
}


