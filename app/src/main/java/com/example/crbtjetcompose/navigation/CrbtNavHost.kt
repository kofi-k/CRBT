package com.example.crbtjetcompose.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.crbt.data.core.data.MusicControllerUiState
import com.crbt.home.navigation.homeScreen
import com.crbt.onboarding.navigation.navigateToOnboarding
import com.crbt.onboarding.navigation.onboardingScreen
import com.crbt.profile.navigation.profileScreen
import com.crbt.services.navigation.PACKAGES_ROUTE
import com.crbt.services.navigation.RECHARGE_ROUTE
import com.crbt.services.navigation.servicesScreen
import com.crbt.subscription.navigation.subscriptionScreen
import com.crbt.ui.core.ui.musicPlayer.CrbtTonesViewModel
import com.example.crbtjetcompose.ui.CrbtAppState


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CrbtNavHost(
    appState: CrbtAppState,
    modifier: Modifier = Modifier,
    startDestination: String,
    crbtTonesViewModel: CrbtTonesViewModel,
    musicControllerUiState: MusicControllerUiState,
) {
    val navController = appState.navController

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            onboardingScreen(
                navigateToHome = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                },
                navController = navController,
            )

            homeScreen(
                navigateToTopUp = {},
                navigateToSubscription = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.SUBSCRIPTIONS)
                },
                crbtTonesViewModel = crbtTonesViewModel,
                navigateToPackages = {
                    navController.navigate(PACKAGES_ROUTE)
                },
                navigateToRecharge = {
                    navController.navigate(RECHARGE_ROUTE)
                },
                navigateToServices = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.SERVICES)
                }
            )
            servicesScreen(
                navController = navController,
            )
            subscriptionScreen(
                navController = navController,
                crbtTonesViewModel = crbtTonesViewModel,
                musicControllerUiState = musicControllerUiState,
            )
            profileScreen(
                navController = navController,
                onLogout = {
                    navController.navigateToOnboarding()
                }
            )
        }
    }
}