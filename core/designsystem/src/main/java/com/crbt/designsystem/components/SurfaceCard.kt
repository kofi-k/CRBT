package com.crbt.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SurfaceCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
) {
    Surface(
        content = { content() },
        shape = MaterialTheme.shapes.large,
        color = color,
        modifier = modifier
    )
}