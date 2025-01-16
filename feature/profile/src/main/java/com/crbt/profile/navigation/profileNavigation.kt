package com.crbt.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.profile.AppInfo
import com.crbt.profile.BugReportingScreen
import com.crbt.profile.OtherSettings
import com.crbt.profile.Profile
import com.crbt.profile.ProfileRoute


const val PROFILE_ROUTE = "profile_route"
const val PROFILE_EDIT_ROUTE = "$PROFILE_ROUTE/edit"
const val BUG_REPORTS = "$PROFILE_ROUTE/bug_reports"
const val APP_INFO = "$PROFILE_ROUTE/app_info"
const val OTHER_SETTINGS = "$PROFILE_ROUTE/other_settings"


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
            onLogout = onLogout,
            onEditProfileClick = navController::navigateToProfileEdit,
            navigateToBugReports = {
                navController.navigate(BUG_REPORTS)
            },
            navigateToOthers = {
                navController.navigate(OTHER_SETTINGS)
            }
        )
    }

    composable(route = PROFILE_EDIT_ROUTE) {
        Profile(
            onSaveSuccess = navController::navigateUp
        )
    }

    composable(route = BUG_REPORTS) {
        BugReportingScreen(
            onBugReportSubmitted = navController::navigateUp
        )
    }

    composable(route = OTHER_SETTINGS) {
        OtherSettings(
            navigateToAppInfo = {
                navController.navigate(APP_INFO)
            }
        )
    }

    composable(route = APP_INFO) {
        AppInfo(
            navigateUp = navController::navigateUp
        )
    }
}

