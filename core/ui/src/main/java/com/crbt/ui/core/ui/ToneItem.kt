package com.crbt.ui.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.itengs.crbt.core.ui.R

@Composable
fun ToneItem(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    trailingContent: @Composable () -> Unit,
    imageUrl: String?,
    @DrawableRes imageRes: Int = R.drawable.core_ui_paps_image,
    colors: ListItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.outlineVariant.copy(
            stronglyDeemphasizedAlpha
        ),
        headlineColor = MaterialTheme.colorScheme.onSurface,
        supportingColor = MaterialTheme.colorScheme.onSurface.copy(
            alpha = slightlyDeemphasizedAlpha
        ),
    ),
) {
    ListItem(
        colors = colors,
        headlineContent = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        trailingContent = trailingContent,
        leadingContent = {
            DynamicAsyncImage(
                imageUrl = imageUrl,
                imageRes = imageRes,
                modifier = imageModifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        },
        modifier = modifier.fillMaxWidth(),
    )
}


@Preview
@Composable
fun PreviewToneItem() {
    ToneItem(
        title = stringResource(id = R.string.core_ui_untitled),
        subtitle = stringResource(id = R.string.core_ui_untitled),
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(CrbtIcons.MoreVert, contentDescription = null)
            }
        },
        imageUrl = "",
    )
}
