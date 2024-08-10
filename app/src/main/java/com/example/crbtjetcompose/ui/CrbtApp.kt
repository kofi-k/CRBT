package com.example.crbtjetcompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.CrbtNavigationBar
import com.crbt.designsystem.components.CrbtNavigationBarItem
import com.crbt.designsystem.components.CrbtTopAppBar
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.example.crbtjetcompose.R
import com.example.crbtjetcompose.navigation.CrbtNavHost
import com.example.crbtjetcompose.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun CrbtApp(appState: CrbtAppState) {
    val destination = appState.currentTopLevelDestination
    val currentRoute = appState.currentDestination?.route
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (destination != null) {
                CrbtBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    modifier =
                    Modifier.testTag("CrbtBottomBar"),
//                        .padding(horizontal = 16.dp)
//                        .padding(bottom = 8.dp)
//                        .clip(MaterialTheme.shapes.extraLarge)
                )
            }
        },
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            // Show the top app bar on top level destinations.
            if (destination != null) {
                CrbtTopAppBar(
                    titleRes = destination.titleTextId,
                    navigationIcon = Icons.Default.Search,
                    navigationIconContentDescription = "",
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    onNavigationClick = {
                        // todo handle navigation
                    },
                    showNavigationIcon = false, // todo handle navigation icon visibility based on destination
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = CrbtIcons.Notifications,
                                contentDescription = CrbtIcons.Notifications.name,
                            )
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
                                        text = DummyUser.user.firstName,
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
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
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
                                Text(text = stringResource(id = destination.titleTextId))
                            }
                        }
                    }
                )
            }

            CrbtNavHost(
                appState = appState,
                modifier = Modifier.fillMaxSize()
            )
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .clip(MaterialTheme.shapes.extraLarge)
        )
    }
}