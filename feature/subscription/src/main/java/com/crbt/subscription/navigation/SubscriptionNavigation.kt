package com.crbt.subscription.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SUBSCRIPTION_ROUTE = "subscriptions_route"

fun NavController.navigateToSubscription(navOptions: NavOptions) =
    navigate(SUBSCRIPTION_ROUTE, navOptions)

fun NavGraphBuilder.subscriptionScreen(
) {
    composable(route = SUBSCRIPTION_ROUTE) {

    }
}