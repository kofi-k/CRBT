package com.crbt.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.model.DummyUser
import com.crbt.data.core.data.model.fullName
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CustomGradientColors
import com.example.crbtjetcompose.feature.profile.R

@Composable
fun ProfileRoute(
    onRewardPointsClicked: () -> Unit,
    onLogout: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val userResult by profileViewModel.userResultState.collectAsStateWithLifecycle()
    val signOutState by profileViewModel.signOutState.collectAsStateWithLifecycle()
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
            when (userResult) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Error -> Unit

                is Result.Success -> {
                    val user = (userResult as Result.Success).data
                    ProfileHeader(
                        onEditProfileClick = onEditProfileClick,
                        userName = user.fullName().ifBlank { DummyUser.user.firstName },
                        phoneNumber = user.phoneNumber.ifBlank { DummyUser.user.phoneNumber },
                        userImageUrl = user.profileUrl
                    )
                }
            }
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
                onClick = {
                    profileViewModel.signOut(signedOut = onLogout)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(imageVector = CrbtIcons.Logout, contentDescription = CrbtIcons.Logout.name)
                Spacer(modifier = Modifier.width(8.dp))
                val text = if (signOutState is SignOutState.Loading) {
                    stringResource(id = R.string.feature_profile_logging_out)
                } else {
                    stringResource(id = R.string.feature_profile_logout)
                }
                Text(text = text)
                AnimatedVisibility(visible = signOutState is SignOutState.Loading) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun ProfileHeader(
    onEditProfileClick: () -> Unit = {},
    userName: String,
    phoneNumber: String,
    userImageUrl: String
) {
    ListCard(
        onClick = onEditProfileClick,
        headlineText = userName,
        leadingContentIcon = CrbtIcons.MoreVert,
        subText = phoneNumber,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                DynamicAsyncImage(
                    imageUrl = userImageUrl,
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


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileRoute(
        onRewardPointsClicked = {},
        onLogout = {}
    )
}
