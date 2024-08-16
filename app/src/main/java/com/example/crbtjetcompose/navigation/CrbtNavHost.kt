package com.example.crbtjetcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.crbt.home.navigation.homeScreen
import com.crbt.onboarding.navigation.ONBOARDING_ROUTE
import com.crbt.onboarding.navigation.navigateToOnboarding
import com.crbt.onboarding.navigation.onboardingScreen
import com.crbt.profile.navigation.profileScreen
import com.crbt.services.navigation.navigateToTopUp
import com.crbt.services.navigation.servicesScreen
import com.crbt.subscription.navigation.navigateToTones
import com.crbt.subscription.navigation.subscriptionScreen
import com.example.crbtjetcompose.ui.CrbtAppState


@Composable
fun CrbtNavHost(
    appState: CrbtAppState,
    modifier: Modifier = Modifier,
    startDestination: String = ONBOARDING_ROUTE, // todo set the start destination based on the user's state as new or not
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardingScreen(
            navigateToHome = {
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            },
            navController = navController
        )
        homeScreen(
            navController = navController,
            navigateToTopUp = navController::navigateToTopUp,
            onPopularTodayClick = navController::navigateToTones
        )
        servicesScreen(
            navigateToHome = {
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            },
            navController = navController
        )
        subscriptionScreen(
            navController = navController,
            onSubscriptionComplete = {
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
            }
        )
        profileScreen(
            navController = navController,
            onLogout = navController::navigateToOnboarding
        )
    }
}