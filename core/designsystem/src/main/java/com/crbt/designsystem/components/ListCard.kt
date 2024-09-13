package com.crbt.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha

@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    clickEnabled: Boolean = true,
    headlineText: String,
    subText: String? = null,
    supportingContent: @Composable (() -> Unit)? = {
        subText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    slightlyDeemphasizedAlpha
                )
            )
        }
    },
    leadingContentIcon: ImageVector,
    leadingContent: @Composable (() -> Unit)? = {
        FilledTonalIconButton(onClick = onClick) {
            Icon(
                imageVector = leadingContentIcon,
                contentDescription = leadingContentIcon.name,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    },
    trailingContent: @Composable (() -> Unit)? = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    overlineContent: @Composable (() -> Unit)? = {},
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .clickable(
                onClick = onClick,
                enabled = clickEnabled
            ),
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
        },
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors,
        overlineContent = overlineContent
    )
}
