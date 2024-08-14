package com.crbt.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crbt.home.AccountHistory
import com.crbt.home.HomeScreen

const val SUBSCRIPTION_ID = "subscription_id"
const val HOME_ROUTE = "home_route"
const val ACCOUNT_HISTORY_ROUTE = "$HOME_ROUTE/account_history$SUBSCRIPTION_ID={$SUBSCRIPTION_ID}"


fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(HOME_ROUTE, navOptions)


fun NavGraphBuilder.homeScreen(
    navController: NavController,
    navigateToTopUp: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            onSubscriptionClick = { subscriptionId ->
                navController.navigate("$ACCOUNT_HISTORY_ROUTE$subscriptionId")
            },
            onNavigateToTopUp = navigateToTopUp
        )
    }
    composable(
        route = ACCOUNT_HISTORY_ROUTE,
        arguments = listOf(navArgument(SUBSCRIPTION_ID) { type = NavType.StringType })
    ) {
        val subscriptionId = it.arguments?.getString(SUBSCRIPTION_ID)
        AccountHistory(subscriptionId = subscriptionId)
    }
}