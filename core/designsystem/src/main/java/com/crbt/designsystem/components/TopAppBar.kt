@file:OptIn(ExperimentalMaterial3Api::class)

package com.crbt.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.crbt.designsystem.theme.CrbtTheme
import com.example.crbtjetcompose.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrbtTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    titleContent: @Composable () -> Unit = {
        Text(text = stringResource(id = titleRes))
    },
    showNavigationIcon: Boolean = true,
) {
    TopAppBar(
        title = titleContent,
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        actions = actions,
        colors = colors,
        modifier = modifier.testTag("FauTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun FauTopAppBarPreview() {
    CrbtTheme {
        CrbtTopAppBar(
            titleRes = R.string.core_designsystem_untitled,
            navigationIcon = Icons.Default.Search,
            navigationIconContentDescription = "Navigation icon",
        )
    }
}
