package com.crbt.ui.core.ui.musicPlayer

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.crbt.domain.PauseSongUseCase
import com.crbt.domain.ResumeSongUseCase
import com.crbt.domain.SeekSongToPositionUseCase
import com.crbt.domain.SkipToNextSongUseCase
import com.crbt.domain.SkipToPreviousSongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SongViewModel @Inject constructor(
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    private val seekSongToPositionUseCase: SeekSongToPositionUseCase,
) : ViewModel() {
    fun onEvent(event: CrbtSongEvent) {
        when (event) {
            CrbtSongEvent.PauseCrbtSong -> pauseMusic()
            CrbtSongEvent.ResumeCrbtSong -> resumeMusic()
            is CrbtSongEvent.SeekCrbtSongToPosition -> seekToPosition(event.position)
            CrbtSongEvent.SkipToNextCrbtSong -> skipToNextSong()
            CrbtSongEvent.SkipToPreviousCrbtSong -> skipToPreviousSong()
        }
    }

    private fun pauseMusic() {
        pauseSongUseCase()
    }

    private fun resumeMusic() {
        resumeSongUseCase()
    }

    private fun skipToNextSong() = skipToNextSongUseCase {

    }

    private fun skipToPreviousSong() = skipToPreviousSongUseCase {

    }

    private fun seekToPosition(position: Long) {
        seekSongToPositionUseCase(position)
    }

    fun calculateColorPalette(drawable: Bitmap, onFinish: (Color) -> Unit) {
        Palette.from(drawable).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

}
