package com.crbt.data.core.data.musicService

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.example.crbtjetcompose.core.model.data.MusicResource

class MusicPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackIndex: Int = 0
    private var musicFiles: List<MusicResource> = emptyList()

    fun setMusicFiles(files: List<MusicResource>) {
        musicFiles = files
    }

    fun play() {
        if (mediaPlayer == null) {
            startPlaying()
        } else {
            mediaPlayer?.start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun next() {
        currentTrackIndex = (currentTrackIndex + 1) % musicFiles.size
        startPlaying()
    }

    fun playTrack(index: Int) {
        if (index in musicFiles.indices) {
            currentTrackIndex = index
            startPlaying()
        }
    }

    private fun startPlaying() {
        if (musicFiles.isEmpty()) {
            // Handle empty list case
            return
        }
        mediaPlayer?.release()
        val track = musicFiles[currentTrackIndex]
        mediaPlayer = MediaPlayer.create(context, Uri.parse(track.data))
        mediaPlayer?.start()
    }


    fun getCurrentTrack(): MusicResource? {
        return if (musicFiles.isNotEmpty()) musicFiles[currentTrackIndex] else null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}