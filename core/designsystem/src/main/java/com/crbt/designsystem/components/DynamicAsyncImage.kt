package com.crbt.designsystem.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.itengs.crbt.core.designsystem.R


@Composable
fun DynamicAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    base64ImageString: String? = null,
    @DrawableRes imageRes: Int = R.drawable.avatar,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    // Decode base64 to ImageBitmap if provided
    val base64Bitmap = remember(base64ImageString) {
        base64ImageString?.let {
            val imageBytes = Base64.decode(it, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
        }
    }

    // Image painter depending on the source
    val painter = if (base64Bitmap != null) {
        isLoading = false
        isError = false
        remember(base64Bitmap) { BitmapPainter(base64Bitmap) }
    } else {
        rememberAsyncImagePainter(
            model = imageUrl,
            onState = { state ->
                isLoading = state is AsyncImagePainter.State.Loading && imageUrl != null
                isError = state is AsyncImagePainter.State.Error
            }
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Image(
            painter = if (!isError && !isLocalInspection && (imageUrl != null || base64Bitmap != null)) {
                painter
            } else {
                painterResource(imageRes)
            },
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}


