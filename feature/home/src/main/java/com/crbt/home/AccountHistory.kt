package com.crbt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.example.crbtjetcompose.core.model.data.mapToUserToneSubscriptions

enum class AccountHistoryTab {
    Payment,
    Subscriptions
}

enum class SubscriptionTabs {
    All,
    Monthly,
    BiWeekly,
    Weekly,
    FreeTrials
}

@Composable
fun AccountHistory(
    subscriptionId: String? = null
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var tabSelected by remember {
            mutableStateOf(
                if (subscriptionId != null) AccountHistoryTab.Subscriptions else AccountHistoryTab.Payment
            )
        }
        AccountHistoryTabRow(
            onTabSelected = {
                tabSelected = it
            },
            selectedTab = tabSelected
        )
        SurfaceCard(
            content = {
                when (tabSelected) {
                    AccountHistoryTab.Payment -> {
                        Payment(modifier = Modifier)
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
    SurfaceCard(
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp),
            ) {
                var tabSelected by remember { mutableStateOf(SubscriptionTabs.All) }
                SubscriptionTabRow(
                    onTabSelected = {
                        tabSelected = it
                        // todo use a view model to filter the subscriptions
                    }
                )
                SubscriptionsFeed(
                    onSubscriptionClick = {},
                    userSubscriptions = DummyTones.tones.mapToUserToneSubscriptions(DummyUser.user),
                )
            }
        }
    )
}

@Composable
fun Payment(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier)
    ) {
        Text(text = "Payment")
    }
}

@Composable
fun AccountHistoryTabRow(
    modifier: Modifier = Modifier,
    onTabSelected: (AccountHistoryTab) -> Unit,
    selectedTab: AccountHistoryTab
) = trace("AccountHistory") {
    var selectedTabIndex by remember { mutableIntStateOf(selectedTab.ordinal) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                divider = {},
                indicator = {},
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding( 8.dp)
            ) {
                AccountHistoryTab.entries.forEachIndexed { index, tab ->
                    val text = when (tab) {
                        AccountHistoryTab.Payment -> "Payment"
                        AccountHistoryTab.Subscriptions -> "Subscriptions"
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = if (selectedTabIndex == index)
                            MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.large
                            )
                            .padding(vertical = 12.dp)
                            .clickable {
                                selectedTabIndex = index
                                onTabSelected(tab)
                            }
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
                    .heightIn(min = 48.dp)
                    .widthIn(min = 48.dp)
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

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {},
        indicator = {},
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .then(modifier)
    ) {

        SubscriptionTabs.entries.forEachIndexed { index, tab ->

            val text = when (tab) {
                SubscriptionTabs.All -> "All"
                SubscriptionTabs.Monthly -> "Monthly"
                SubscriptionTabs.BiWeekly -> "Bi-Weekly"
                SubscriptionTabs.Weekly -> "Weekly"
                SubscriptionTabs.FreeTrials -> "Free Trials"
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (selectedTabIndex == index)
                    MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clickable {
                        selectedTabIndex = index
                        onTabSelected(tab)
                    }
            )
        }
    }
}


@Preview
@Composable
fun AccountHistoryTabRowPreview() {
    AccountHistoryTabRow(
        onTabSelected = {},
        selectedTab = AccountHistoryTab.Subscriptions
    )
}

@Preview
@Composable
fun SubscriptionTabRowPreview() {
    SubscriptionTabRow(
        onTabSelected = {}
    )
}

@ThemePreviews
@Composable
fun AccountHistoryPreview() {
    CrbtTheme {
        AccountHistory()
    }
}
