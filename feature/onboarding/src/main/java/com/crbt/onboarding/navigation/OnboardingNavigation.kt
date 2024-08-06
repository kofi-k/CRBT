package com.crbt.onboarding.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val ONBOARDING_ROUTE = "onboarding_route"

fun NavController.navigateToOnboarding(navOptions: NavOptions) =
    navigate(ONBOARDING_ROUTE, navOptions)

fun NavGraphBuilder.onboardingScreen(
    onNavigateToHome: () -> Unit
) {
    composable(route = ONBOARDING_ROUTE) {


        BackHandler {
            // todo custom back navigation logic on onboarding screen
        }
    }

}