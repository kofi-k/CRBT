package com.crbt.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.crbt.common.core.common.result.Result
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.ui.core.ui.EmptyContent
import com.crbt.ui.core.ui.ToneItem
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import com.example.crbtjetcompose.feature.home.R


@Composable
fun RecentSubscription(
    modifier: Modifier = Modifier,
    navigateToSubscription: () -> Unit,
    userSubscriptions: Result<CrbtSongResource?>,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.feature_home_recent_subscription_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = CrbtIcons.ArrowRight,
                    contentDescription = CrbtIcons.ArrowRight.name
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
        ) {
            when (userSubscriptions) {
                is Result.Success -> {
                    userSubscriptions.data?.let {
                        SubscriptionsFeed(
                            userSubscriptions = listOf(it),
                            onSubscriptionClick = {},
                            modifier = Modifier
                                .heightIn(
                                    max = max(
                                        200.dp,
                                        with(LocalDensity.current) { 200.sp.toDp() })
                                )
                                .fillMaxWidth(),
                        )
                    }
                }

                is Result.Error -> {
                    EmptyContent(
                        description = userSubscriptions.exception.message
                            ?: stringResource(id = R.string.feature_home_no_subscriptions),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        errorContent = {
                            Icon(
                                imageVector = CrbtIcons.Info,
                                contentDescription = CrbtIcons.Info.name,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    )
                }

                Result.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionsFeed(
    modifier: Modifier = Modifier,
    onSubscriptionClick: (toneId: String?) -> Unit,
    userSubscriptions: List<CrbtSongResource>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 0.dp),
    ) {
        items(
            items = userSubscriptions,
            key = { it.id }
        ) { tone ->
            ToneItem(
                title = tone.songTitle,
                imageUrl = tone.profile,
                subtitle = tone.artisteName,
                colors = ListItemDefaults.colors(),
                trailingContent = {
                    SubscriptionTrailingContent(
                        title = tone.price,
                        subtitle = tone.subscriptionType
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSubscriptionClick(tone.id)
                    }
            )
        }
    }
}

@Composable
fun SubscriptionTrailingContent(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_home_balance, title),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha)
        )
    }

}

@ThemePreviews
@Composable
fun PreviewRecentSubscription() {
    CrbtTheme {
        RecentSubscription(
            userSubscriptions = Result.Error(Exception("No songs found")),
            navigateToSubscription = {}
        )
    }
}