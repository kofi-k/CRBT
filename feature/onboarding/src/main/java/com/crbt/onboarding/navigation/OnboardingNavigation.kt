package com.crbt.onboarding.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.crbt.onboarding.OnboardingFinishedScreen
import com.crbt.onboarding.OnboardingScreen
import com.crbt.onboarding.ui.Profile


const val ONBOARDING_ROUTE = "onboarding_route"
const val ONBOARDING_PROFILE_ROUTE = "onboarding_profile_route"
const val ONBOARDING_COMPLETE_ROUTE = "onboarding_complete_route"


fun NavController.navigateToOnboarding() =
    navigate(ONBOARDING_ROUTE)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.onboardingScreen(
    navigateToHome: () -> Unit,
    navController: NavController,
) {
    composable(route = ONBOARDING_ROUTE) {
        OnboardingScreen(
            onOTPVerified = {
                navController.navigate(ONBOARDING_PROFILE_ROUTE) {
                    popUpTo(ONBOARDING_ROUTE) {
                        inclusive = true
                    }
                }
            },
        )
    }

    composable(route = ONBOARDING_PROFILE_ROUTE) {
        Profile(
            onOnboardingComplete = {
                navController.navigate(ONBOARDING_COMPLETE_ROUTE) {
                    popUpTo(ONBOARDING_PROFILE_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = ONBOARDING_COMPLETE_ROUTE) {
        OnboardingFinishedScreen(
            navigateToHome = {
                navController.popBackStack(ONBOARDING_COMPLETE_ROUTE, true)
                navigateToHome()
            }
        )
    }
}