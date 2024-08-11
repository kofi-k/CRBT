package com.crbt.services.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.services.RechargeScreen
import com.crbt.services.ServicesScreen
import com.crbt.services.TopUpCheckoutScreen

const val SERVICES_ROUTE = "services_route"
const val TOPUP_ROUTE = "top_up_route"
const val TOPUP_CHECKOUT_ROUTE = "top_up_checkout_route"


fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)

fun NavGraphBuilder.servicesScreen(
    navigateToHome: () -> Unit,
    navController: NavController,
) {
    composable(route = SERVICES_ROUTE) {
        ServicesScreen(
            onCheckClick = { /*TODO*/ },
            onPackageClick = { /*TODO*/ },
            onRechargeClick = { /*TODO*/ },
            onTransferClick = { /*TODO*/ },
            onCallBackClick = { /*TODO*/ },
            onBuyClick = { /*TODO*/ }
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