package com.crbt.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.phoneAuth.SignOutState
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.bodyFontFamily
import com.crbt.ui.core.ui.MessageSnackbar
import com.itengs.crbt.core.model.data.fullName
import com.itengs.crbt.feature.profile.R
import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand
import kotlinx.coroutines.launch


@Composable
fun ProfileRoute(
    onEditProfileClick: () -> Unit = {},
    navigateToBugReports: () -> Unit,
    navigateToOthers: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val userPreferenceUiState by profileViewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSigningOut by remember { mutableStateOf(false) }

    ProfileScreen(
        onEditProfileClick = onEditProfileClick,
        onLogout = {
            isSigningOut = true
            scope.launch {
                when (val state = profileViewModel.signOut()) {
                    is SignOutState.Success -> {}
                    is SignOutState.Error -> {
                        isSigningOut = false
                        snackbarHostState.showSnackbar(
                            state.message,
                            duration = SnackbarDuration.Short
                        )
                    }

                    else -> isSigningOut = true
                }
            }
        },
        signingOut = isSigningOut,
        userPreferenceUiState = userPreferenceUiState,
        onLanguageCheckChange = { code ->
            profileViewModel.saveLanguageCode(code)
        },
        navigateToBugReports = navigateToBugReports,
        updateThemeBrand = profileViewModel::updateThemeBrand,
        updateDarkThemeConfig = profileViewModel::updateDarkThemeConfig,
        updateDynamicColorPreference = profileViewModel::updateDynamicColorPreference,
        navigateToOthers = navigateToOthers
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    signingOut: Boolean,
    userPreferenceUiState: SettingsUiState,
    onLanguageCheckChange: (String) -> Unit = {},
    navigateToBugReports: () -> Unit,
    updateThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    updateDarkThemeConfig: (
        darkThemeConfig: DarkThemeConfig
    ) -> Unit,
    updateDynamicColorPreference: (
        useDynamicColor: Boolean
    ) -> Unit,
    navigateToOthers: () -> Unit
) {
    when (userPreferenceUiState) {
        is SettingsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
            }
        }

        is SettingsUiState.Success -> {
            val userData = userPreferenceUiState.userPreferencesData

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
                        onEditProfileClick = onEditProfileClick,
                        userName = userData.fullName(),
                        phoneNumber = userData.phoneNumber,
                        userImageUrl = userData.profileUrl
                    )
                }

                item {
                    ProfileSettings(
                        onLanguageCheckChange = onLanguageCheckChange,
                        userLangPref = userData.languageCode,
                        onPermissionCheckChange = { _, _ -> },
                        rewardPoints = userData.rewardPoints.toString()
                    )
                }

                item {
                    ThemeSettingsScreen(
                        updateThemeBrand = updateThemeBrand,
                        updateDarkThemeConfig = updateDarkThemeConfig,
                        updateDynamicColorPreference = updateDynamicColorPreference,
                        themeSettings = userPreferenceUiState.settings
                    )
                }

                item {
                    SurfaceCard(
                        content = {
                            ListCard(
                                onClick = navigateToBugReports,
                                headlineText = stringResource(id = R.string.feature_profile_report_bug_title),
                                leadingContentIcon = CrbtIcons.BugReport,
                                trailingContent = {},
                                supportingContent = {
                                    Text(
                                        text = stringResource(id = R.string.feature_profile_report_bug_decriptiom),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                leadingContent = {
                                    Icon(
                                        imageVector = CrbtIcons.BugReport,
                                        contentDescription = CrbtIcons.BugReport.name,
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.size(62.dp)
                                    )
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    headlineColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    supportingColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                ),
                            )
                        },
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }

                item {
                    ListCard(
                        onClick = navigateToOthers,
                        headlineText = stringResource(id = R.string.feature_profile_others),
                        leadingContentIcon = CrbtIcons.Help,
                        trailingContent = {},
                        supportingContent = {
                            Text(
                                text = stringResource(id = R.string.feature_profile_others_description),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = CrbtIcons.Help,
                                contentDescription = CrbtIcons.Help.name,
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }

                item {
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(
                            imageVector = CrbtIcons.Logout,
                            contentDescription = CrbtIcons.Logout.name
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val text = if (signingOut) {
                            stringResource(id = R.string.feature_profile_logging_out)
                        } else {
                            stringResource(id = R.string.feature_profile_logout)
                        }
                        Text(text = text)
                    }
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
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                DynamicAsyncImage(
                    base64ImageString = userImageUrl,
                    imageRes = com.itengs.crbt.core.ui.R.drawable.core_ui_avatar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettings(
    onLanguageCheckChange: (String) -> Unit,
    userLangPref: String,
    onPermissionCheckChange: (String, Boolean) -> Unit = { _, _ -> },
    rewardPoints: String
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    SurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        content = {
            Column {
                ListCard(
                    onClick = {
                        showBottomSheet = true
                    },
                    headlineText = stringResource(id = R.string.feature_profile_reward_points),
                    subText = stringResource(id = R.string.feature_profile_reward_points_description),
                    leadingContentIcon = CrbtIcons.RewardPoints,
                    trailingContent = {
                        IconButton(onClick = {
                            showBottomSheet = true
                        }) {
                            Icon(
                                imageVector = CrbtIcons.ArrowRight,
                                contentDescription = CrbtIcons.ArrowRight.name
                            )
                        }
                    }
                )

                LanguageSettings(
                    onLanguageCheckChange = onLanguageCheckChange,
                    selectedLanguage = userLangPref
                )

                PermissionSettings(
                    onPermissionCheckChange = onPermissionCheckChange
                )
            }
        }
    )

    if (showBottomSheet) {
        RewardPointsBottomSheet(
            onDismiss = { showBottomSheet = false },
            rewardPoints = rewardPoints
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardPointsBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
    rewardPoints: String
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.feature_profile_reward),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Column {
                        Text(
                            text = rewardPoints,
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontFamily = bodyFontFamily
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.feature_profile_total_reward_points),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }


                Text(
                    text = stringResource(id = R.string.feature_profile_crbt_reward_points),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = stringResource(id = R.string.feature_profile_crbt_reward_points_info),
                    style = MaterialTheme.typography.labelMedium
                )


                ProcessButton(
                    onClick = onDismiss,
                    text = stringResource(id = R.string.feature_profile_reward_points_sheet_action),
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    )
}


@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileRoute(
        onEditProfileClick = {},
        navigateToBugReports = {},
        navigateToOthers = {}
    )
}
