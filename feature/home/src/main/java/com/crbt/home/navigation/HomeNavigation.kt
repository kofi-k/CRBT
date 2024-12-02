package com.crbt.home.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeScreen(
    navigateToTopUp: () -> Unit,
    onPopularTodayClick: (String) -> Unit,
    navigateToSubscription: () -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            navigateToSubscription = navigateToSubscription,
            onNavigateToTopUp = navigateToTopUp,
            onPopularTodayClick = onPopularTodayClick
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