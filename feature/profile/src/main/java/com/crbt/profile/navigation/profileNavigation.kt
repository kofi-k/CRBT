package com.crbt.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val PROFILE_ROUTE = "profile_route"
const val PROFILE_EDIT_ROUTE = "$PROFILE_ROUTE/edit"


fun NavController.navigateToProfile(navOptions: NavOptions) =
    navigate(PROFILE_ROUTE, navOptions)

fun NavController.navigateToProfileEdit() =
    navigate(PROFILE_EDIT_ROUTE)

fun NavGraphBuilder.profileScreen(
    onNavigateToHome: () -> Unit,
    navController: NavController
) {
    composable(route = PROFILE_ROUTE) {
        ProfileScreen(
            onRewardPointsClicked = { /*TODO*/ },
            onPaymentMethodsClicked = { /*TODO*/ },
            onCurrencyClicked = { /*TODO*/ },
            onLanguageClicked = { /*TODO*/ },
            onPermissionsClicked = { /*TODO*/ },
            onLogout = { /*TODO*/ },
            onEditProfileClick = {
                navController.navigateToProfileEdit()
            }
        )
    }

    composable(route = PROFILE_EDIT_ROUTE) {
        Profile(
            onSaveButtonClicked = onNavigateToHome
        )
    }
}

