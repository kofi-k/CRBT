package com.example.crbtjetcompose.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.util.INTERNET_SPEED_CHECK_URL
import com.crbt.designsystem.components.CrbtNavigationBar
import com.crbt.designsystem.components.CrbtNavigationBarItem
import com.crbt.designsystem.components.CrbtTopAppBar
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtBackground
import com.crbt.designsystem.theme.CrbtGradientBackground
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.LocalGradientColors
import com.crbt.designsystem.theme.extremelyDeemphasizedAlpha
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.home.navigation.ACCOUNT_HISTORY_ROUTE
import com.crbt.home.navigation.HOME_ROUTE
import com.crbt.onboarding.navigation.ONBOARDING_COMPLETE_ROUTE
import com.crbt.onboarding.navigation.ONBOARDING_PROFILE_ROUTE
import com.crbt.onboarding.navigation.ONBOARDING_ROUTE
import com.crbt.profile.navigation.PROFILE_EDIT_ROUTE
import com.crbt.services.navigation.PACKAGES_ROUTE
import com.crbt.services.navigation.TOPUP_CHECKOUT_ROUTE
import com.crbt.services.navigation.TOPUP_ROUTE
import com.crbt.subscription.navigation.ADD_SUBSCRIPTION_ROUTE
import com.crbt.subscription.navigation.SUBSCRIPTION_COMPLETE_ROUTE
import com.crbt.subscription.navigation.TONES_ROUTE
import com.crbt.ui.core.ui.launchCustomChromeTab
import com.example.crbtjetcompose.R
import com.example.crbtjetcompose.navigation.CrbtNavHost
import com.example.crbtjetcompose.navigation.TopLevelDestination

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun CrbtApp(
    appState: CrbtAppState,
) {
    val destination = appState.currentTopLevelDestination
    val currentRoute = appState.currentDestination?.route
    val context = LocalContext.current

    val showNavIcon =
        destination != null && appState.currentDestination.isTopLevelDestinationInHierarchy(
            destination
        ) && currentRoute != destination.name

    val showNavigationBars = currentRoute !in listOf(
        ONBOARDING_ROUTE,
        ONBOARDING_COMPLETE_ROUTE,
        ONBOARDING_PROFILE_ROUTE,
        ADD_SUBSCRIPTION_ROUTE
    )

    val showBottomBar = currentRoute !in listOf(
        ONBOARDING_ROUTE,
        ONBOARDING_COMPLETE_ROUTE,
        ONBOARDING_PROFILE_ROUTE,
    )


    val titleRes = when {
        destination != null -> destination.titleTextId
        else -> when (currentRoute) {
            TOPUP_ROUTE -> com.example.crbtjetcompose.feature.services.R.string.feature_services_topup
            TOPUP_CHECKOUT_ROUTE, SUBSCRIPTION_COMPLETE_ROUTE -> com.example.crbtjetcompose.feature.profile.R.string.feature_profile_payments
            ACCOUNT_HISTORY_ROUTE -> com.example.crbtjetcompose.feature.home.R.string.feature_home_account_history
            PROFILE_EDIT_ROUTE -> com.example.crbtjetcompose.feature.profile.R.string.feature_profile_title
            TONES_ROUTE -> com.example.crbtjetcompose.feature.subscription.R.string.feature_subscription_tones
            ADD_SUBSCRIPTION_ROUTE -> com.example.crbtjetcompose.feature.subscription.R.string.feature_subscription_add_subscription_title
            PACKAGES_ROUTE -> com.example.crbtjetcompose.feature.services.R.string.feature_services_packages
            else -> com.example.crbtjetcompose.core.designsystem.R.string.core_designsystem_untitled
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    val isLoggedIn by appState.isLoggedIn.collectAsStateWithLifecycle()
    val isProfileSetupComplete by appState.isProfileSetupComplete.collectAsStateWithLifecycle()
    val internetSpeed by appState.internetSpeed.collectAsStateWithLifecycle()
    val userData by appState.userData.collectAsStateWithLifecycle()

    val startDestination: String = when (isLoggedIn) {
        true -> when (isProfileSetupComplete) {
            true -> HOME_ROUTE
            false -> ONBOARDING_PROFILE_ROUTE
        }

        false -> ONBOARDING_ROUTE
    }

    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = Indefinite,
            )
        }
    }



    CrbtBackground(
        modifier = Modifier
    ) {
        CrbtGradientBackground(
            gradientColors = LocalGradientColors.current
        ) {
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    if (showBottomBar) {
                        CrbtBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier =
                            Modifier
                                .testTag("CrbtBottomBar")
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 24.dp,
                                        topEnd = 24.dp
                                    )
                                )
                        )
                    }
                },
                topBar = {
                    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
                    if (showNavigationBars) {
                        CrbtTopAppBar(
                            titleRes = titleRes,
                            navigationIcon = CrbtIcons.ArrowBack,
                            navigationIconContentDescription = "",
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                            ),
                            onNavigationClick = {
                                appState.navController.navigateUp()
                            },
                            showNavigationIcon = !showNavIcon,
                            actions = {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    val color by animateColorAsState(
                                        targetValue = if (isOffline || internetSpeed == 0.0) {
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = extremelyDeemphasizedAlpha
                                            )
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                        label = "Internet Speed Color"
                                    )

                                    TextButton(
                                        onClick = {
                                            launchCustomChromeTab(
                                                context = context,
                                                uri = Uri.parse(INTERNET_SPEED_CHECK_URL),
                                                toolbarColor = backgroundColor
                                            )
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {

                                        Text(
                                            text = stringResource(
                                                id = R.string.internet_speed,
                                                internetSpeed
                                            ),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Icon(
                                            imageVector = CrbtIcons.SwapVert,
                                            contentDescription = CrbtIcons.SwapVert.name,
                                            modifier = Modifier.size(12.dp),
                                            tint = color
                                        )
                                    }
                                    when (destination) {
                                        TopLevelDestination.PROFILE -> {
                                            IconButton(onClick = {
                                                appState.navigateToProfileEdit()
                                            }
                                            ) {
                                                Icon(
                                                    imageVector = CrbtIcons.Edit,
                                                    contentDescription = CrbtIcons.Edit.name,
                                                )
                                            }
                                        }

                                        else -> {
                                            IconButton(onClick = { /*TODO*/ }) {
                                                Icon(
                                                    imageVector = CrbtIcons.Notifications,
                                                    contentDescription = CrbtIcons.Notifications.name,
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            titleContent = {
                                when (destination) {
                                    TopLevelDestination.HOME -> {
                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.core_designsystem_welecome),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    slightlyDeemphasizedAlpha
                                                ),
                                            )
                                            Text(
                                                text = (userData as Result.Success).data.firstName,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                            )
                                        }
                                    }

                                    TopLevelDestination.SERVICES -> {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.core_designsystem_services_ussd),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                            )
                                            Text(
                                                text = stringResource(id = R.string.core_designsystem_what_would_you_like_to_do),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    slightlyDeemphasizedAlpha
                                                ),
                                            )
                                        }
                                    }

                                    else -> {
                                        Text(text = stringResource(id = titleRes))
                                    }
                                }
                            }
                        )
                    }
                }
            ) { padding ->
                when (userData) {
                    is Result.Loading -> CircularProgressIndicator()
                    is Result.Error -> Unit
                    is Result.Success -> CrbtNavHost(
                        appState = appState,
                        startDestination = startDestination,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    )
                }
            }
        }
    }
}

/*will customize this to look like figma design */
@Composable
private fun CrbtBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    CrbtNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            CrbtNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = {
//                    Text(stringResource(destination.iconTextId))
                },
                modifier = Modifier,
                alwaysShowLabel = false
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false


@ThemePreviews
@Composable
fun PreviewBottomBar() {
    CrbtTheme {
        CrbtBottomBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = {},
            currentDestination = null,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
        )
    }
}