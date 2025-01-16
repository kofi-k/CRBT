package com.itengs.crbt.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import com.itengs.crbt.ui.CrbtAppState


@SuppressLint("NewApi")
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
            modifier = modifier,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
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

enum class ScaleTransitionDirection {
    INWARDS, OUTWARDS
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}