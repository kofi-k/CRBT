package com.crbt.services.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crbt.services.RechargeScreen
import com.crbt.services.ServicesRoute
import com.crbt.services.TopUpCheckoutScreen
import com.crbt.services.TopupScreen
import com.crbt.services.packages.PackagesScreen

const val TOPUP_AMOUNT_ARG = "topup_amount"
const val SERVICES_ROUTE = "services_route"
const val TOPUP_ROUTE = "$SERVICES_ROUTE/topup"
const val TOPUP_CHECKOUT_ROUTE =
    "$SERVICES_ROUTE/topup_checkout?$TOPUP_AMOUNT_ARG={$TOPUP_AMOUNT_ARG}"

const val PACKAGES_ROUTE = "$SERVICES_ROUTE/packages"
const val RECHARGE_ROUTE = "$SERVICES_ROUTE/recharge"

fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)


fun NavController.navigateToTopUpCheckout(amount: String, navOptions: NavOptions? = null) =
    navigate("$SERVICES_ROUTE/topup_checkout?$TOPUP_AMOUNT_ARG=$amount", navOptions)

fun NavGraphBuilder.servicesScreen(
    navController: NavController,
) {
    composable(route = SERVICES_ROUTE) {
        ServicesRoute(
            navigateToPackages = {
                navController.navigate(PACKAGES_ROUTE)
            },
            navigateToRecharge = {
                navController.navigate(RECHARGE_ROUTE)
            }
        )
    }

    composable(route = TOPUP_ROUTE) {
        TopupScreen(
            onTopUpClick = { amount ->
                navController.navigateToTopUpCheckout(amount)
            }
        )
    }

    composable(
        route = TOPUP_CHECKOUT_ROUTE,
        arguments = listOf(
            navArgument(TOPUP_AMOUNT_ARG) { type = NavType.StringType }
        )
    ) {
        TopUpCheckoutScreen(
            onCompleteTopUp = {
                navController.navigate(SERVICES_ROUTE) {
                    popUpTo(SERVICES_ROUTE) {
                        inclusive = true
                    }
                }
            },
        )
    }

    composable(route = PACKAGES_ROUTE) {
        PackagesScreen()
    }

    composable(route = RECHARGE_ROUTE) {
        RechargeScreen(navigateUp = {})
    }
}