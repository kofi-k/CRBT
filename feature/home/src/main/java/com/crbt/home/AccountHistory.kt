package com.crbt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.core.model.data.mapToUserToneSubscriptions

enum class AccountHistoryTab {
    Payment,
    Subscriptions
}

enum class SubscriptionTabs {
    All,
    Monthly,
    Yearly,
    FreeTrials
}

@Composable
fun AccountHistory() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var tabSelected by remember { mutableStateOf(AccountHistoryTab.Subscriptions) }
        AccountHistoryTabRow(
            onTabSelected = {
                tabSelected = it
            }
        )
        SurfaceCard(
            content = {
                when (tabSelected) {
                    AccountHistoryTab.Payment -> {
                        Payment()
                    }

                    AccountHistoryTab.Subscriptions -> {
                        Subscriptions()
                    }
                }
            }
        )
    }
}


@Composable
fun Subscriptions() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        var tabSelected by remember { mutableStateOf(SubscriptionTabs.All) }
        SubscriptionTabRow(
            onTabSelected = {
                tabSelected = it
            }
        )
        when (tabSelected) {
            SubscriptionTabs.All -> {
                SubscriptionsFeed(
                    onSubscriptionClick = {},
                    userSubscriptions = DummyTones.tones.mapToUserToneSubscriptions(DummyUser.user),
                )
            }

            SubscriptionTabs.Monthly -> {
                Text(text = "Monthly")
            }

            SubscriptionTabs.Yearly -> {
                Text(text = "Yearly")
            }

            SubscriptionTabs.FreeTrials -> {
                Text(text = "Free Trials")
            }
        }
    }
}

@Composable
fun Payment() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Payment")
    }
}

@Composable
fun AccountHistoryTabRow(
    modifier: Modifier = Modifier,
    onTabSelected: (AccountHistoryTab) -> Unit
) = trace("AccountHistory") {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                divider = {},
                indicator = {},
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AccountHistoryTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        text = {
                            val text = when (tab) {
                                AccountHistoryTab.Payment -> "Payment"
                                AccountHistoryTab.Subscriptions -> "Subscriptions"
                            }
                            Text(text = text)
                        },
                        selected = selectedTabIndex == index,
                        selectedContentColor = MaterialTheme.colorScheme.surface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            selectedTabIndex = index
                            onTabSelected(tab)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedTabIndex == index) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            FilledIconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
            ) {
                Icon(imageVector = CrbtIcons.MoreVert, contentDescription = CrbtIcons.MoreVert.name)
            }
        }
    }
}

@Composable
fun SubscriptionTabRow(
    modifier: Modifier = Modifier,
    onTabSelected: (SubscriptionTabs) -> Unit
) = trace("SubscriptionTabRow") {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {},
        indicator = {},
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(modifier)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            state = rememberLazyListState(),
            modifier = Modifier
        ) {
            items(
                items = SubscriptionTabs.entries.toList(),
                key = { it.ordinal }
            ) {
                val text = when (it) {
                    SubscriptionTabs.All -> "All"
                    SubscriptionTabs.Monthly -> "Monthly"
                    SubscriptionTabs.Yearly -> "Yearly"
                    SubscriptionTabs.FreeTrials -> "Free Trials"
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selectedTabIndex == it.ordinal)
                        MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            color = if (selectedTabIndex == it.ordinal) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clickable {
                            selectedTabIndex = it.ordinal
                            onTabSelected(it)
                        }
                )
            }
        }
    }

}


@Preview
@Composable
fun AccountHistoryTabRowPreview() {
    AccountHistoryTabRow(
        onTabSelected = {}
    )
}

@Preview
@Composable
fun SubscriptionTabRowPreview() {
    SubscriptionTabRow(
        onTabSelected = {}
    )
}

@Preview
@Composable
fun AccountHistoryPreview() {
    AccountHistory()
}
