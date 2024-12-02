package com.example.crbtjetcompose.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.crbt.home.navigation.homeScreen
import com.crbt.onboarding.navigation.navigateToOnboarding
import com.crbt.onboarding.navigation.onboardingScreen
import com.crbt.profile.navigation.profileScreen
import com.crbt.services.navigation.servicesScreen
import com.crbt.subscription.navigation.navigateToAddSubscription
import com.crbt.subscription.navigation.subscriptionScreen
import com.crbt.ui.core.ui.musicPlayer.SharedCrbtMusicPlayerViewModel
import com.example.crbtjetcompose.ui.CrbtAppState


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CrbtNavHost(
    appState: CrbtAppState,
    modifier: Modifier = Modifier,
    startDestination: String,
    sharedCrbtMusicPlayerViewModel: SharedCrbtMusicPlayerViewModel
) {
    val navController = appState.navController
    val musicControllerUiState = sharedCrbtMusicPlayerViewModel.musicControllerUiState
    val isProfileSetupComplete by appState.isProfileSetupComplete.collectAsStateWithLifecycle()

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
                isProfileSetupComplete = isProfileSetupComplete
            )

            homeScreen(
                navigateToTopUp = {},
                onPopularTodayClick = { toneId ->
                    navController.navigateToAddSubscription(toneId, false)
                },
                navigateToSubscription = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.SUBSCRIPTIONS)
                }
            )
            servicesScreen(
                navController = navController,
                navigateToTopLevel = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                }
            )
            subscriptionScreen(
                navController = navController,
                musicControllerUiState = musicControllerUiState,
                onSubscriptionSuccess = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.SUBSCRIPTIONS)
                }
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