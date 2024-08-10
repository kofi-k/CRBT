package com.crbt.services.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.services.ServicesScreen

const val SERVICES_ROUTE = "services_route"

fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)

fun NavGraphBuilder.servicesScreen(
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
}