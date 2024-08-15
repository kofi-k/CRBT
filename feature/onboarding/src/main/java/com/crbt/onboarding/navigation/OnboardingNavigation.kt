package com.crbt.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.crbt.onboarding.OnboardingFinishedScreen
import com.crbt.onboarding.OnboardingScreen
import com.crbt.onboarding.Profile


const val ONBOARDING_ROUTE = "onboarding_route"
const val ONBOARDING_PROFILE_ROUTE = "onboarding_profile_route"
const val ONBOARDING_COMPLETE_ROUTE = "onboarding_complete_route"


fun NavController.navigateToOnboarding() =
    navigate(ONBOARDING_ROUTE) {
        popUpTo(ONBOARDING_ROUTE) {
            inclusive = false
        }
    }

fun NavGraphBuilder.onboardingScreen(
    navigateToHome: () -> Unit,
    navController: NavController
) {
    composable(route = ONBOARDING_ROUTE) {
        OnboardingScreen(
            onOTPVerified = {
                navController.navigate(ONBOARDING_PROFILE_ROUTE)
            },
        )
    }

    composable(route = ONBOARDING_PROFILE_ROUTE) {
        Profile(
            onSaveButtonClicked = {
                navController.navigate(ONBOARDING_COMPLETE_ROUTE)
            }
        )
    }

    composable(route = ONBOARDING_COMPLETE_ROUTE) {
        OnboardingFinishedScreen(
            navigateToHome = navigateToHome
        )
    }
}