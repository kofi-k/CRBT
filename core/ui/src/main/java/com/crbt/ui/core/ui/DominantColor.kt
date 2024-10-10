package com.crbt.ui.core.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import java.net.URL

@Composable
fun rememberDominantColor(imageUrl: String?, defaultColor: Color = Color.White): Color {
    var dominantColor by remember { mutableStateOf(defaultColor) }

    LaunchedEffect(imageUrl) {
        imageUrl?.let {
            // Use Palette to get the dominant color
            val bitmap = loadImageBitmap(imageUrl)
            bitmap?.let {
                val palette = Palette.from(it).generate()
                palette?.let {
                    dominantColor = Color(palette.getDominantColor(defaultColor.toArgb()))
                }
            }
        }
    }

    return dominantColor
}


fun isColorDark(color: Color): Boolean {
    val darkness = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return darkness >= 0.5
}

private fun loadImageBitmap(imageUrl: String): Bitmap? {
    try {
        val url = URL(imageUrl)
        return BitmapFactory.decodeStream(url.openConnection().getInputStream())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


