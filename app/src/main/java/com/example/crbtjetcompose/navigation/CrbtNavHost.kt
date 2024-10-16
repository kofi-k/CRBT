package com.example.crbtjetcompose.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.crbt.home.navigation.HOME_ROUTE
import com.crbt.home.navigation.homeScreen
import com.crbt.onboarding.navigation.navigateToOnboarding
import com.crbt.onboarding.navigation.onboardingScreen
import com.crbt.profile.navigation.profileScreen
import com.crbt.services.navigation.servicesScreen
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
                navController = navController
            )
            homeScreen(
                navController = navController,
                navigateToTopUp = {
                    navController.popBackStack(HOME_ROUTE, true)
                    appState.navigateToTopUp()
                },
                onPopularTodayClick = {
                    navController.popBackStack(HOME_ROUTE, true)
                    appState.navigateToTones()
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
                navigateToTopLevel = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                },
                musicControllerUiState = musicControllerUiState
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