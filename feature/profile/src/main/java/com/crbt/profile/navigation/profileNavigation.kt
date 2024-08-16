package com.crbt.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.profile.Profile
import com.crbt.profile.ProfileRoute


const val PROFILE_ROUTE = "profile_route"
const val PROFILE_EDIT_ROUTE = "$PROFILE_ROUTE/edit"


fun NavController.navigateToProfile(navOptions: NavOptions) =
    navigate(PROFILE_ROUTE, navOptions)

fun NavController.navigateToProfileEdit() =
    navigate(PROFILE_EDIT_ROUTE)

fun NavGraphBuilder.profileScreen(
    onLogout: () -> Unit,
    navController: NavController
) {
    composable(route = PROFILE_ROUTE) {
        ProfileRoute(
            onLogout = {
                navController.popBackStack(PROFILE_ROUTE, inclusive = true)
                onLogout()
            },
            onEditProfileClick = {
                navController.navigateToProfileEdit()
            },
            onRewardPointsClicked = { /*TODO*/ },
        )
    }

    composable(route = PROFILE_EDIT_ROUTE) {
        Profile(
            onSaveButtonClicked = { navController.navigateUp() }
        )
    }
}

