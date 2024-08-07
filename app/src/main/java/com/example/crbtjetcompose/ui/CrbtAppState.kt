package com.example.crbtjetcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.crbt.home.navigation.HOME_ROUTE
import com.crbt.onboarding.navigation.ONBOARDING_ROUTE
import com.crbt.profile.navigation.PROFILE_ROUTE
import com.crbt.services.navigation.SERVICES_ROUTE
import com.crbt.subscription.navigation.SUBSCRIPTION_ROUTE
import com.example.crbtjetcompose.navigation.TopLevelDestination

@Composable
fun rememberCrbtAppState(
    navController: NavHostController = rememberNavController(),
): CrbtAppState {
    return remember(
        navController,
    ) {
        CrbtAppState(
            navController = navController,
        )
    }
}


@Stable
class CrbtAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            PROFILE_ROUTE -> TopLevelDestination.PROFILE
            SERVICES_ROUTE -> TopLevelDestination.SERVICES
            HOME_ROUTE -> TopLevelDestination.HOME
            SUBSCRIPTION_ROUTE -> TopLevelDestination.SUBSCRIPTIONS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = navController.currentDestination?.route != ONBOARDING_ROUTE


    /**
     * Map of top level destinations to be used in the TopBar and BottomBar . The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries


    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.HOME -> navController.navigate(HOME_ROUTE, topLevelNavOptions)
                TopLevelDestination.SERVICES -> navController.navigate(
                    SERVICES_ROUTE,
                    topLevelNavOptions
                )

                TopLevelDestination.SUBSCRIPTIONS -> navController.navigate(
                    SUBSCRIPTION_ROUTE,
                    topLevelNavOptions
                )

                TopLevelDestination.PROFILE -> navController.navigate(
                    PROFILE_ROUTE,
                    topLevelNavOptions
                )
            }
        }
    }

}
