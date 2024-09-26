package com.crbt.data.core.data.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

const val USER_PROFILE_IMAGE = "profile_image.jpg"

fun copyImageToInternalStorage(context: Context, uri: Uri): Uri? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, USER_PROFILE_IMAGE)
    val outputStream = FileOutputStream(file)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    return Uri.fromFile(file)
}
