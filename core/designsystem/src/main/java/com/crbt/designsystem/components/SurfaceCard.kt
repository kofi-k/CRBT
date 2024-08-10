package com.crbt.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SurfaceCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        content = { content() },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
    )
}