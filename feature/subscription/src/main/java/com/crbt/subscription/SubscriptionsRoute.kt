package com.crbt.subscription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.SearchToolbar
import com.example.crbtjetcompose.feature.subscription.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SubscriptionsRoute(
    onTonesClick: () -> Unit,
    onAlbumClick: () -> Unit,
) {
    val viewModel: SubscriptionViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.checkPermissions(context)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var searchQuery by remember { mutableStateOf("") }

        SearchToolbar(
            searchQuery = searchQuery,
            onSearchQueryChanged = {
                searchQuery = it
            },
            onSearchTriggered = {
                //TODO
            },
            onBackClick = { /*TODO*/ },
            showNavigationIcon = false,
            modifier = Modifier
        )
        Categories(
            onCategoryClick = {},
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ListCard(
                onClick = onTonesClick,
                trailingContent = {
                    IconButton(onClick = onTonesClick) {
                        Icon(imageVector = CrbtIcons.Add, contentDescription = CrbtIcons.Add.name)
                    }
                },
                headlineText = stringResource(id = R.string.feature_subscription_tones),
                subText = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface),
                leadingContentIcon = CrbtIcons.Tones
            )

            ListCard(
                onClick = onAlbumClick,
                trailingContent = {
                    IconButton(onClick = onAlbumClick) {
                        Icon(imageVector = CrbtIcons.Add, contentDescription = CrbtIcons.Add.name)
                    }
                },
                headlineText = stringResource(id = R.string.feature_subscription_albums),
                subText = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface),
                leadingContentIcon = CrbtIcons.Album
            )
        }

    }
}

@ThemePreviews
@Composable
fun SubscriptionsScreenPreview() {
    SubscriptionsRoute(
        onTonesClick = {},
        onAlbumClick = {},
    )
}
