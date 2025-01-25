package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun FadedEdge(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    brush: Brush = Brush.verticalGradient(
        listOf(Color.Black, Color.Transparent),
    ),
) {
    Box(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.DstIn
                )
            },
    ) {
        content()
    }
}