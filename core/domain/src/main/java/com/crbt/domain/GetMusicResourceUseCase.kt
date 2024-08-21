package com.crbt.domain

import com.crbt.data.core.data.repository.MusicRepository
import com.example.crbtjetcompose.core.model.data.MusicResource
import javax.inject.Inject

class GetMusicResourceUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): List<MusicResource> {
        return musicRepository.getMusicFiles()
    }
}