package com.crbt.ui.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.icon.CrbtIcons

@Composable
fun ToneItem(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes subtitle: Int,
    trailingContent: @Composable () -> Unit,
    imageUrl: String
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(id = title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = stringResource(id = subtitle),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = trailingContent,
        leadingContent = {
            DynamicAsyncImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .size(60.dp)
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
        title = com.example.crbtjetcompose.core.ui.R.string.core_ui_untitled,
        subtitle = com.example.crbtjetcompose.core.ui.R.string.core_ui_untitled,
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(CrbtIcons.MoreVert, contentDescription = null)
            }
        },
        imageUrl = "",
    )
}
