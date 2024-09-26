package com.crbt.services.packages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tracing.trace
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.services.ServiceSheetContainer
import com.crbt.services.ServicesViewModel
import com.crbt.ui.core.ui.GiftPurchasePhoneNumber
import com.example.crbtjetcompose.core.model.data.CrbtPackage
import com.example.crbtjetcompose.core.model.data.PackageItem
import com.example.crbtjetcompose.feature.services.R
import kotlinx.coroutines.launch


@Composable
fun PackagesScreen() {
    val viewModel: ServicesViewModel = hiltViewModel()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        PackageContent(
            modifier = Modifier.padding(bottom = 16.dp),
            onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
            onPurchasePackage = {}, //TODO implement onPurchasePackage
            actionEnabled = viewModel.isPhoneNumberValid,
            actionLoading = false, //TODO implement actionLoading state
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageContent(
    modifier: Modifier = Modifier,
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    actionEnabled: Boolean,
    actionLoading: Boolean,
    onPurchasePackage: (String) -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var expandedItemId by remember { mutableStateOf<String?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var isGiftPurchase by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val pagerState = rememberPagerState(pageCount = { CrbtPackage.dummyPackages.size })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedTabIndex = page
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PackageTabs(
            modifier = Modifier.fillMaxWidth(),
            onTabSelected = {
                selectedTabIndex = it
            },
            selectedTabIndex = selectedTabIndex,
            tabs = CrbtPackage.dummyPackages
        )

        TabContent(
            packageItems = PackageItem.dummyPackages.filter {
                it.packageId == CrbtPackage.dummyPackages[selectedTabIndex].id
            },
            onBuyClick = {
                showBottomSheet = true
                isGiftPurchase = false
            },
            onGiftClick = {
                showBottomSheet = true
                isGiftPurchase = true
            },
            expandedItemId = expandedItemId,
            onExpandItem = {
                expandedItemId = if (expandedItemId == it) null else it
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight()
        )

        AnimatedVisibility(
            visible = showBottomSheet,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            PurchasePackageBottomSheet(
                onConfirmClick = {
                    showBottomSheet = false
                    scope.launch {
                        sheetState.hide()
                        onPurchasePackage(expandedItemId!!)
                    }
                },
                onDismissClick = {
                    showBottomSheet = false
                    scope.launch {
                        sheetState.hide()
                    }
                },
                isGiftPurchase = isGiftPurchase,
                onPhoneNumberChanged = onPhoneNumberChanged,
                price = PackageItem.dummyPackages.first().price,
                packageName = PackageItem.dummyPackages.first().title,
                sheetState = sheetState,
                actionEnabled = if (isGiftPurchase) actionEnabled else true,
                actionLoading = actionLoading
            )
        }
    }
}


@Composable
fun PackageTabs(
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit,
    selectedTabIndex: Int,
    tabs: List<CrbtPackage>,
) = trace("PackageTabs") {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {},
        indicator = {},
        modifier = modifier,
        containerColor = Color.Transparent
    ) {
        tabs.forEachIndexed { index, tab ->
            Text(
                text = tab.packageName,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clickable { onTabSelected(index) }
            )
        }
    }
}

@Composable
fun TabContent(
    modifier: Modifier = Modifier,
    packageItems: List<PackageItem>,
    onBuyClick: (String) -> Unit,
    onGiftClick: (String) -> Unit,
    expandedItemId: String?,
    onExpandItem: (String) -> Unit,
) {
    SurfaceCard(
        modifier = modifier,
        content = {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                packageItemsFeed(
                    packageItems = packageItems,
                    onBuyClick = onBuyClick,
                    onGiftClick = onGiftClick,
                    expandedItemId = expandedItemId,
                    onExpandItem = onExpandItem
                )
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasePackageBottomSheet(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    isGiftPurchase: Boolean,
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    price: String,
    packageName: String,
    actionEnabled: Boolean,
    actionLoading: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val title = if (isGiftPurchase) {
            stringResource(id = R.string.feature_services_gift_purchase_contact_info) to
                    stringResource(id = R.string.feature_services_gift_button)
        } else {
            stringResource(
                id = R.string.feature_services_purchase_confirmation,
                packageName,
                price
            ) to stringResource(id = R.string.feature_services_buy)
        }
        ServiceSheetContainer(
            onDismiss = onDismissClick,
            title = title.first,
            sheetState = sheetState,
            content = {
                if (isGiftPurchase) {
                    GiftPurchasePhoneNumber(
                        onPhoneNumberChanged = onPhoneNumberChanged,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                ButtonActionRow(
                    onConfirmClick = onConfirmClick,
                    onDismissClick = onDismissClick,
                    modifier = Modifier.fillMaxWidth(),
                    actionText = title.second,
                    actionLoading = actionLoading,
                    actionEnabled = if (isGiftPurchase) actionEnabled else true
                )
            },
        )
    }
}

@Composable
fun ButtonActionRow(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionText: String,
    actionLoading: Boolean,
    actionEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = onDismissClick,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.feature_services_cancel),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        ProcessButton(
            onClick = onConfirmClick,
            text = actionText,
            colors = ButtonDefaults.filledTonalButtonColors(),
            isProcessing = actionLoading,
            isEnabled = actionEnabled,
            modifier = Modifier.wrapContentSize(),
        )
    }
}


fun LazyListScope.packageItemsFeed(
    packageItems: List<PackageItem>,
    onBuyClick: (String) -> Unit,
    onGiftClick: (String) -> Unit,
    onExpandItem: (String) -> Unit,
    expandedItemId: String?,
) {
    items(packageItems.size) { index ->
        val item = packageItems[index]
        ItemCard(
            id = item.id,
            title = item.title,
            description = item.description,
            price = item.price,
            metaData = item.metaData,
            validity = item.validity,
            customImage = "",
            onBuyClick = onBuyClick,
            onGiftClick = onGiftClick,
            expanded = { expandedItemId == item.id },
            onExpandItem = onExpandItem,
        )
        if (index < packageItems.size - 1) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


@Composable
fun ItemCard(
    id: String,
    title: String,
    description: String,
    price: String,
    metaData: String,
    validity: String,
    customImage: String,
    onBuyClick: (String) -> Unit,
    onGiftClick: (String) -> Unit,
    expanded: (String) -> Boolean,
    onExpandItem: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .animateContentSize()
    ) {
        ListCard(
            onClick = { onExpandItem(id) },
            headlineText = title,
            leadingContent = {
                if (customImage.isEmpty()) {
                    Icon(
                        imageVector = CrbtIcons.Packages,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.large)
                    ) {
                        DynamicAsyncImage(imageUrl = customImage)
                    }
                }
            },
            overlineContent = {},
            supportingContent = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.animateContentSize()
                ) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    if (expanded(id) && metaData.isNotEmpty()) {
                        Text(
                            text = metaData,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                slightlyDeemphasizedAlpha
                            )
                        )
                    }
                }
            },
            leadingContentIcon = CrbtIcons.Language,
            trailingContent = {
                Column(
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    Text(
                        text = "$price ETB",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            brush = Brush.linearGradient(CustomGradientColors)
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    AnimatedVisibility(
                        visible = expanded(id),
                        enter = slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Text(
                            text = validity,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                slightlyDeemphasizedAlpha
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        if (expanded(id)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            ) {
                ProcessButton(
                    onClick = { onGiftClick(id) },
                    text = stringResource(id = R.string.feature_services_gift_button),
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ProcessButton(
                    onClick = { onBuyClick(id) },
                    text = stringResource(id = R.string.feature_services_buy),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    CrbtTheme {
        ItemCard(
            id = "1",
            title = "5G Package",
            description = "5G Description",
            price = "1000",
            validity = "1 day",
            metaData = "5G MetaData",
            customImage = "",
            onBuyClick = {},
            onGiftClick = {},
            expanded = { true },
            onExpandItem = {}
        )
    }
}

//GiftPurchaseContentPreview
@Preview(showBackground = true)
@Composable
fun GiftPurchaseContentPreview() {
    CrbtTheme {
        GiftPurchasePhoneNumber(
            onPhoneNumberChanged = { _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

