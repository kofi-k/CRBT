package com.crbt.services.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.services.RechargeScreen
import com.crbt.services.ServicesRoute
import com.crbt.services.TopUpCheckoutScreen
import com.crbt.services.packages.PackagesScreen

const val TOPUP_AMOUNT_ARG = "topup_amount"
const val SERVICES_ROUTE = "services_route"
const val TOPUP_ROUTE = "$SERVICES_ROUTE/topup"
const val TOPUP_CHECKOUT_ROUTE =
    "$SERVICES_ROUTE/topup_checkout$TOPUP_AMOUNT_ARG={$TOPUP_AMOUNT_ARG}"

const val PACKAGES_ROUTE = "$SERVICES_ROUTE/packages"

fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)

fun NavController.navigateToTopUp() =
    navigate(TOPUP_ROUTE)

fun NavGraphBuilder.servicesScreen(
    navController: NavController,
    navigateToTopLevel: () -> Unit
) {
    composable(route = SERVICES_ROUTE) {
        ServicesRoute(
            navigateToPackages = {
                navController.navigate(PACKAGES_ROUTE)
            },
            navigateToRecharge = {
                navController.navigate(TOPUP_ROUTE)
            },
        )
    }

    composable(route = TOPUP_ROUTE) {
        RechargeScreen(
            onTopUpClick = {
                navController.navigate(TOPUP_CHECKOUT_ROUTE)
            }
        )
    }

    composable(route = TOPUP_CHECKOUT_ROUTE) {
        TopUpCheckoutScreen(
            onCompleteTopUp = {
                navController.navigate(SERVICES_ROUTE) {
                    popUpTo(SERVICES_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = PACKAGES_ROUTE) {
        PackagesScreen()
    }
}