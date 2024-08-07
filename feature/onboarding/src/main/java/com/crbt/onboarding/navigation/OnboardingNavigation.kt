package com.crbt.onboarding.navigation

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.onboarding.OnboardingScreen
import com.crbt.onboarding.OnboardingViewModel
import com.crbt.onboarding.Profile


const val ONBOARDING_ROUTE = "onboarding_route"
const val ONBOARDING_PROFILE = "onboarding_profile_route"


fun NavController.navigateToOnboarding(navOptions: NavOptions) =
    navigate(ONBOARDING_ROUTE, navOptions)

fun NavGraphBuilder.onboardingScreen(
    onNavigateToOnboardingProfile: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable(route = ONBOARDING_ROUTE) {
        val viewModel: OnboardingViewModel = hiltViewModel()
        val screenData = viewModel.onboardingScreenData
        val onboardingSetupData = viewModel.onboardingSetupData
        OnboardingScreen(
            onNextClicked = { viewModel.onNextClicked() },
            onOTPVerified = onNavigateToOnboardingProfile,
            screenData = screenData,
            onboardingSetupData = onboardingSetupData,
            onPhoneNumberEntered = viewModel::onPhoneNumberEntered,
            onLanguageSelected = viewModel::onLanguageSelected,
            isNextEnabled = viewModel.isNextEnabled

        )

//        BackHandler {
//            viewModel.onPreviousClicked()  //todo handle back press in onboarding
//        }
    }

    composable(route = ONBOARDING_PROFILE) {
        Profile(
            onUserProfileResponse = { _, _, _ -> },
            onEmailCheckChanged = {},
            onSaveButtonClicked = {
                onNavigateToHome()
            }
        )
    }

}