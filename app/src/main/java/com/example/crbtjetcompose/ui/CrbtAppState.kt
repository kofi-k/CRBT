package com.example.crbtjetcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UserManager
import com.crbt.data.core.data.util.NetworkMonitor
import com.crbt.home.navigation.HOME_ROUTE
import com.crbt.home.navigation.navigateToHome
import com.crbt.profile.navigation.PROFILE_ROUTE
import com.crbt.profile.navigation.navigateToProfile
import com.crbt.profile.navigation.navigateToProfileEdit
import com.crbt.services.navigation.SERVICES_ROUTE
import com.crbt.services.navigation.navigateToServices
import com.crbt.subscription.navigation.SUBSCRIPTION_ROUTE
import com.crbt.subscription.navigation.navigateToSubscription
import com.example.crbtjetcompose.core.model.data.isProfileSetupComplete
import com.example.crbtjetcompose.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberCrbtAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
    userRepository: CrbtPreferencesRepository,
    userManager: UserManager
): CrbtAppState {
    return remember(
        navController,
    ) {
        CrbtAppState(
            navController = navController,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            userRepository = userRepository,
            userManager = userManager
        )
    }
}


@Stable
class CrbtAppState(
    val navController: NavHostController,
    networkMonitor: NetworkMonitor,
    userManager: UserManager,
    coroutineScope: CoroutineScope,
    userRepository: CrbtPreferencesRepository
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

    val userData = userRepository.userPreferencesData
        .onStart { Result.Loading }
        .mapLatest { Result.Success(it) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Result.Loading,
        )


    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val isLoggedIn = userManager.isLoggedIn
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val isProfileSetupComplete = userRepository.userPreferencesData
        .mapLatest { it.isProfileSetupComplete() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )


    val internetSpeed = networkMonitor.internetSpeed
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0,
        )


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
    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination,
    ) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(HOME_ROUTE) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.HOME ->
                    navController.navigateToHome(topLevelNavOptions)

                TopLevelDestination.SERVICES ->
                    navController.navigateToServices(topLevelNavOptions)

                TopLevelDestination.SUBSCRIPTIONS ->
                    navController.navigateToSubscription(topLevelNavOptions)

                TopLevelDestination.PROFILE ->
                    navController.navigateToProfile(topLevelNavOptions)
            }
        }
    }

    fun navigateToProfileEdit() = navController.navigateToProfileEdit()


}
