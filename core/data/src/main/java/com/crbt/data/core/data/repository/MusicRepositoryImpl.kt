package com.crbt.data.core.data.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.crbt.common.core.common.network.CrbtDispatchers
import com.crbt.common.core.common.network.Dispatcher
import com.example.crbtjetcompose.core.model.data.MusicResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @Dispatcher(CrbtDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : MusicRepository {


    override suspend fun getMusicFiles(): List<MusicResource> = withContext(ioDispatcher) {
        val musicFiles = mutableListOf<MusicResource>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val mediaCursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        mediaCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)
                val data = cursor.getString(dataColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val album = cursor.getString(albumColumn)

                // Query for album art
                val albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
                val albumArtProjection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
                val albumArtSelection = "${MediaStore.Audio.Albums._ID} = ?"
                val albumArtCursor = contentResolver.query(
                    albumArtUri,
                    albumArtProjection,
                    albumArtSelection,
                    arrayOf(albumId.toString()),
                    null
                )

                var albumArt: String? = null
                albumArtCursor?.use { albumCursor ->
                    if (albumCursor.moveToFirst()) {
                        albumArt =
                            albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                    }
                }

                musicFiles += MusicResource(
                    id = id,
                    name = name,
                    artist = artist,
                    duration = duration,
                    data = data,
                    album = album,
                    coverArt = albumArt
                )
            }
        }
        musicFiles
    }
}