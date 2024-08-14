package com.crbt.services.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.services.RechargeScreen
import com.crbt.services.ServicesScreen
import com.crbt.services.TopUpCheckoutScreen

const val TOPUP_AMOUNT_ARG = "topup_amount"
const val SERVICES_ROUTE = "services_route"
const val TOPUP_ROUTE = "$SERVICES_ROUTE/topup"
const val TOPUP_CHECKOUT_ROUTE =
    "$SERVICES_ROUTE/topup_checkout$TOPUP_AMOUNT_ARG={$TOPUP_AMOUNT_ARG}"


fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)

fun NavController.navigateToTopUp() =
    navigate(TOPUP_ROUTE)

fun NavGraphBuilder.servicesScreen(
    navigateToHome: () -> Unit,
    navController: NavController,
) {
    composable(route = SERVICES_ROUTE) {
        ServicesScreen(
            onPackageClick = { /*TODO*/ },
            onRechargeClick = {
                navController.navigate(TOPUP_ROUTE)
            },
            onTransferClick = { /*TODO*/ },
            onCallBackClick = { /*TODO*/ },
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
            onCompleteTopUp = navigateToHome
        )
    }
}