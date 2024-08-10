package com.crbt.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.home.AccountHistory
import com.crbt.home.HomeScreen


const val HOME_ROUTE = "home_route"
const val ACCOUNT_HISTORY_ROUTE = "account_history_route"


fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    navController: NavController,
    navigateToTopUp: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            onSubscriptionClick = {
//                navController.navigate(ACCOUNT_HISTORY_ROUTE)
            },
            onNavigateToTopUp = navigateToTopUp
        )
    }
    composable(route = ACCOUNT_HISTORY_ROUTE) {
        AccountHistory()
    }
}