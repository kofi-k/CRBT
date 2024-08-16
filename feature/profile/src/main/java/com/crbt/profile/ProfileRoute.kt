package com.crbt.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import com.crbt.data.core.data.CRBTSettingsData
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.ui.core.ui.permissionIcons
import com.example.crbtjetcompose.feature.profile.R

@Composable
fun ProfileRoute(
    onRewardPointsClicked: () -> Unit,
    onLogout: () -> Unit,
    onEditProfileClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeader(
                onEditProfileClick = onEditProfileClick
            )
        }

        item {
            ProfileSettings(
                onRewardPointsClicked = onRewardPointsClicked,
                onPaymentMethodsClicked = {},
                onCurrencyClicked = {},
                onLanguageClicked = {},
                onPermissionCheckChange = { _, _ -> }
            )
            Spacer(modifier = Modifier.heightIn(min = 16.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(imageVector = CrbtIcons.Logout, contentDescription = CrbtIcons.Logout.name)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.feature_profile_logout))
            }
        }
    }
}


@Composable
fun ProfileHeader(
    onEditProfileClick: () -> Unit = {}
) {
    ListCard(
        onClick = onEditProfileClick,
        headlineText = DummyUser.user.firstName,
        leadingContentIcon = CrbtIcons.MoreVert,
        subText = DummyUser.user.phoneNumber,
        leadingContent = {
            Box(
                modifier = Modifier.size(50.dp)
            ) {
                DynamicAsyncImage(
                    imageUrl = "",
                    imageRes = com.example.crbtjetcompose.core.ui.R.drawable.avatar
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .drawWithCache {
                val brush = Brush.linearGradient(
                    CustomGradientColors
                )
                onDrawBehind {
                    drawRoundRect(
                        brush,
                        cornerRadius = CornerRadius(24.dp.toPx())
                    )
                }
            },
    )
}

@Composable
fun ProfileSettings(
    onRewardPointsClicked: () -> Unit,
    onPaymentMethodsClicked: () -> Unit,
    onCurrencyClicked: () -> Unit,
    onLanguageClicked: (String) -> Unit = {},
    onPermissionCheckChange: (String, Boolean) -> Unit = { _, _ -> }
) {
    SurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        content = {
            Column {
                ListCard(
                    onClick = onRewardPointsClicked,
                    headlineText = stringResource(id = R.string.feature_profile_reward_points),
                    subText = stringResource(id = R.string.feature_profile_reward_points_description),
                    leadingContentIcon = CrbtIcons.RewardPoints,
                    trailingContent = {
                        IconButton(onClick = onRewardPointsClicked) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )

                ListCard(
                    onClick = onPaymentMethodsClicked,
                    headlineText = stringResource(id = R.string.feature_profile_payment_methods),
                    subText = stringResource(id = R.string.feature_profile_payment_methods_description),
                    leadingContentIcon = CrbtIcons.PaymentMethods,
                    trailingContent = {
                        IconButton(onClick = onPaymentMethodsClicked) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )

                ListCard(
                    onClick = onCurrencyClicked,
                    headlineText = stringResource(id = R.string.feature_profile_currency),
                    subText = stringResource(id = R.string.feature_profile_currency_description),
                    leadingContentIcon = CrbtIcons.Dollar,
                    trailingContent = {
                        IconButton(onClick = onCurrencyClicked) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )

                LanguageSettings(
                    onLanguageCheckChange = onLanguageClicked
                )

                PermissionSettings(
                    onPermissionCheckChange = onPermissionCheckChange
                )
            }
        }
    )
}

@Composable
fun LanguageSettings(
    onLanguageCheckChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")
    var selectedLanguage by remember { mutableStateOf(CRBTSettingsData.languages.first().id) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListCard(
            onClick = {
                expanded = !expanded
            },
            headlineText = stringResource(id = R.string.feature_profile_language),
            subText = stringResource(id = R.string.feature_profile_language_description),
            leadingContentIcon = CrbtIcons.Language,
            trailingContent = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = CrbtIcons.ArrowRight,
                        contentDescription = CrbtIcons.ArrowRight.name,
                        modifier = Modifier.rotate(rotateIcon)
                    )
                }
            }
        )
        if (expanded) {
            Column {
                CRBTSettingsData.languages.forEach { language ->
                    ListCard(
                        onClick = {
                            onLanguageCheckChange(language.id)
                            selectedLanguage = language.id
                        },
                        headlineText = stringResource(id = language.name),
                        leadingContentIcon = CrbtIcons.Language,
                        leadingContent = {},
                        trailingContent = {
                            RadioButton(
                                selected = selectedLanguage == language.id,
                                onClick = {
                                    onLanguageCheckChange(language.id)
                                    selectedLanguage = language.id
                                    expanded = false
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun PermissionSettings(
    onPermissionCheckChange: (String, Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")
    var allowedPermissions by remember { mutableStateOf(setOf<String>()) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListCard(
            onClick = { expanded = !expanded },
            headlineText = stringResource(id = R.string.feature_profile_permissions),
            subText = stringResource(id = R.string.feature_profile_permissions_description),
            leadingContentIcon = CrbtIcons.Permissions,
            trailingContent = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = CrbtIcons.ArrowRight,
                        contentDescription = CrbtIcons.ArrowRight.name,
                        modifier = Modifier.rotate(rotateIcon)
                    )
                }
            }
        )
        if (expanded) {
            AllowedPermissions(
                onPermissionCheckChange = { permissionId, isSelected ->
                    allowedPermissions = if (isSelected) {
                        allowedPermissions.plus(permissionId)
                    } else {
                        allowedPermissions.minus(permissionId)
                    }
                    onPermissionCheckChange(permissionId, isSelected)
                },
                allowedPermissions = allowedPermissions
            )
        }
    }
}


@Composable
fun AllowedPermissions(
    onPermissionCheckChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    allowedPermissions: Set<String>
) = trace("AllowedPermissions") {
    val lazyGridState = rememberLazyGridState()

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier
                .heightIn(max = max(200.dp, with(LocalDensity.current) { 200.sp.toDp() }))
                .fillMaxWidth(),
        ) {
            items(
                items = CRBTSettingsData.permissions,
                key = { it.id }
            ) {
                SinglePermissionButton(
                    name = stringResource(it.name),
                    permissionId = it.id,
                    icon = permissionIcons[it.name] ?: CrbtIcons.Permissions,
                    isSelected = it.id in allowedPermissions,
                    onClick = onPermissionCheckChange
                )
            }
        }
    }
}

@Composable
private fun SinglePermissionButton(
    name: String,
    permissionId: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
) = trace("SinglePermissionButton") {
    Surface(
        modifier = Modifier
            .width(220.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surfaceBright,
        selected = isSelected,
        onClick = {
            onClick(permissionId, !isSelected)
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = {
                    onClick(permissionId, it)
                }
            )
        }
    }
}


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileRoute(
        onRewardPointsClicked = {},
        onLogout = {}
    )
}

@ThemePreviews
@Composable
fun PreviewAllowedPermissions() {
    CrbtTheme {
        AllowedPermissions(
            onPermissionCheckChange = { _, _ -> },
            allowedPermissions = setOf("1", "2", "3")
        )
    }
}