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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import java.net.URL

@Composable
fun rememberDominantColorWithReadableText(
    imageUrl: String?,
    defaultColor: Color = Color.White,
    fallbackTextColor: Color = Color.Black
): Pair<Color, Color> {
    var dominantColor by remember { mutableStateOf(defaultColor) }
    var readableTextColor by remember { mutableStateOf(fallbackTextColor) }

    LaunchedEffect(imageUrl) {
        imageUrl?.let {
            val bitmap = loadImageBitmap(imageUrl)
            bitmap?.let {
                val palette = Palette.from(it).generate()
                palette?.let {
                    val dominantArgb = palette.getDominantColor(defaultColor.toArgb())
                    dominantColor = Color(dominantArgb)

                    // Calculate contrast ratio and determine the best readable color
                    readableTextColor = getReadableTextColor(Color(dominantArgb), fallbackTextColor)
                }
            }
        }
    }

    return dominantColor to readableTextColor
}


fun getReadableTextColor(
    backgroundColor: Color,
    fallbackTextColor: Color
): Color {
    val whiteContrast = calculateContrast(backgroundColor, Color.White)
    val blackContrast = calculateContrast(backgroundColor, Color.Black)

    // Return the color with the higher contrast ratio
    return if (whiteContrast > blackContrast) Color.White else fallbackTextColor
}

fun calculateContrast(color1: Color, color2: Color): Double {
    val luminance1 = color1.luminance()
    val luminance2 = color2.luminance()

    return if (luminance1 > luminance2) {
        (luminance1 + 0.05) / (luminance2 + 0.05)
    } else {
        (luminance2 + 0.05) / (luminance1 + 0.05)
    }
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


