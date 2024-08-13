package com.crbt.profile.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CustomGradientColors
import com.example.crbtjetcompose.feature.profile.R

@Composable
fun ProfileScreen(
    onRewardPointsClicked: () -> Unit,
    onPaymentMethodsClicked: () -> Unit,
    onCurrencyClicked: () -> Unit,
    onLanguageClicked: () -> Unit,
    onPermissionsClicked: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader()

        ProfileSettings(
            onRewardPointsClicked = onRewardPointsClicked,
            onPaymentMethodsClicked = onPaymentMethodsClicked,
            onCurrencyClicked = onCurrencyClicked,
            onLanguageClicked = onLanguageClicked,
            onPermissionsClicked = onPermissionsClicked,
        )

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


@Composable
fun ProfileHeader() {
    ListCard(
        onClick = { /*TODO*/ },
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
    onLanguageClicked: () -> Unit,
    onPermissionsClicked: () -> Unit,
) {
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
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

                ListCard(
                    onClick = onLanguageClicked,
                    headlineText = stringResource(id = R.string.feature_profile_language),
                    subText = stringResource(id = R.string.feature_profile_language_description),
                    leadingContentIcon = CrbtIcons.Language,
                    trailingContent = {
                        IconButton(onClick = onLanguageClicked) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )

                ListCard(
                    onClick = onPermissionsClicked,
                    headlineText = stringResource(id = R.string.feature_profile_permissions),
                    subText = stringResource(id = R.string.feature_profile_permissions_description),
                    leadingContentIcon = CrbtIcons.Permissions,
                    trailingContent = {
                        IconButton(onClick = onPermissionsClicked) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )
            }
        }
    )
}


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onRewardPointsClicked = {},
        onPaymentMethodsClicked = {},
        onCurrencyClicked = {},
        onLanguageClicked = {},
        onPermissionsClicked = {},
        onLogout = {}
    )
}