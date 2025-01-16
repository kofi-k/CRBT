package com.crbt.profile

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.util.CRBT_ADMIN_PORTAL
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.launchCustomChromeTab
import com.itengs.crbt.feature.profile.R

@Composable
fun OtherSettings(
    navigateToAppInfo: () -> Unit
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ListCard(
            onClick = {
                launchCustomChromeTab(
                    context = context,
                    uri = Uri.parse(CRBT_ADMIN_PORTAL),
                    toolbarColor = backgroundColor
                )
            },
            headlineText = stringResource(id = R.string.feature_profile_terms_and_policy),
            leadingContentIcon = CrbtIcons.Description,
            trailingContent = {},
            supportingContent = null,
            leadingContent = {
                Icon(
                    imageVector = CrbtIcons.Description,
                    contentDescription = CrbtIcons.Description.name,
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
        ListCard(
            onClick = navigateToAppInfo,
            headlineText = stringResource(id = R.string.feature_profile_app_info),
            leadingContentIcon = CrbtIcons.Info,
            trailingContent = {},
            supportingContent = null,
            leadingContent = {
                Icon(
                    imageVector = CrbtIcons.Info,
                    contentDescription = CrbtIcons.Info.name,
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
    }
}