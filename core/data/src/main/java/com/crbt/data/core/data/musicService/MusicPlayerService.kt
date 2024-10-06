package com.crbt.data.core.data.musicService

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject


sealed class PlayerState {
    data object Idle : PlayerState()
    data object Loading : PlayerState()
    data object Playing : PlayerState()
    data object Paused : PlayerState()
    data class Buffering(val percent: Int) : PlayerState()
    data class Error(val message: String) : PlayerState()
    data object Completed : PlayerState()
}


class MusicPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(
        url: String,
        onBufferingUpdate: (Int) -> Unit,
        onError: (String) -> Unit,
        onCompletion: () -> Unit
    ) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }

        mediaPlayer?.apply {
            try {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()

                setAudioAttributes(audioAttributes)

                setDataSource(url)

                setOnPreparedListener {
                    Log.d("MusicPlayer", "Prepared, starting playback.")
                    start()
                }

                setOnBufferingUpdateListener { _, percent ->
                    onBufferingUpdate(percent)
                }

                setOnErrorListener { _, what, extra ->
                    onError("Error: $what, Extra: $extra")
                    true
                }

                setOnCompletionListener {
                    onCompletion()
                }

                prepareAsync()
            } catch (e: IOException) {
                onError("Failed to play media: ${e.message}")
            }
        }
    }


    fun pause() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
