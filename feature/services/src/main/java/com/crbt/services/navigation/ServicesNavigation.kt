package com.crbt.services.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crbt.services.RechargeScreen
import com.crbt.services.ServicesRoute
import com.crbt.services.packages.PackagesScreen

const val SERVICES_ROUTE = "services_route"

const val PACKAGES_ROUTE = "$SERVICES_ROUTE/packages"
const val RECHARGE_ROUTE = "$SERVICES_ROUTE/recharge"

fun NavController.navigateToServices(navOptions: NavOptions) =
    navigate(SERVICES_ROUTE, navOptions)


@RequiresApi(Build.VERSION_CODES.O)
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

    composable(route = PACKAGES_ROUTE) {
        PackagesScreen()
    }

    composable(route = RECHARGE_ROUTE) {
        RechargeScreen(navigateUp = {})
    }
}