package com.crbt.subscription.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crbt.subscription.CrbtSubscribeScreen
import com.crbt.subscription.SubscriptionCheckout
import com.crbt.subscription.SubscriptionsRoute
import com.crbt.subscription.TonesScreen

const val TONE_ID = "tone_id"
const val SUBSCRIPTION_ROUTE = "subscriptions_route"
const val TONES_ROUTE = "$SUBSCRIPTION_ROUTE/tones$TONE_ID={$TONE_ID}"
const val GIFT_SUBSCRIPTION_ROUTE = "$SUBSCRIPTION_ROUTE/gift_subscription$TONE_ID={$TONE_ID}"
const val ADD_SUBSCRIPTION_ROUTE = "$SUBSCRIPTION_ROUTE/add_subscription$TONE_ID={$TONE_ID}"
const val SUBSCRIPTION_COMPLETE_ROUTE = "$SUBSCRIPTION_ROUTE/subscription_complete"

fun NavController.navigateToSubscription(navOptions: NavOptions) =
    navigate(SUBSCRIPTION_ROUTE, navOptions)

fun NavController.navigateToTones(toneId: String? = null, navOptions: NavOptions? = null) =
    navigate("$TONES_ROUTE$toneId", navOptions)

fun NavGraphBuilder.subscriptionScreen(
    navController: NavController,
    onSubscriptionComplete: () -> Unit
) {
    composable(route = SUBSCRIPTION_ROUTE) {
        SubscriptionsRoute(
            onCategoryClick = { /*TODO*/ },
            onTonesClick = navController::navigateToTones,
            onAlbumClick = navController::navigateToTones
        )
    }
    composable(
        route = TONES_ROUTE,
        arguments = listOf(navArgument(TONE_ID) { type = NavType.StringType })
    ) {
        val toneIdArg = it.arguments?.getString(TONE_ID)
        TonesScreen(
            onSubscriptionToneClick = { toneId ->
                navController.navigate("$ADD_SUBSCRIPTION_ROUTE$toneId")
            },
            onGiftSubscriptionClick = { toneId ->
                navController.navigate("$GIFT_SUBSCRIPTION_ROUTE$toneId")
            }
        )
    }

    composable(
        route = GIFT_SUBSCRIPTION_ROUTE,
        arguments = listOf(navArgument(TONE_ID) { type = NavType.StringType })
    ) {
        val toneIdArg = it.arguments?.getString(TONE_ID)

        CrbtSubscribeScreen(
            onSubscriptionComplete = {
                navController.navigate(SUBSCRIPTION_COMPLETE_ROUTE) {
                    popUpTo(SUBSCRIPTION_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(
        route = ADD_SUBSCRIPTION_ROUTE,
        arguments = listOf(navArgument(TONE_ID) { type = NavType.StringType })
    ) {
        val toneIdArg = it.arguments?.getString(TONE_ID)
        CrbtSubscribeScreen(
            onSubscriptionComplete = {
                navController.navigate(SUBSCRIPTION_COMPLETE_ROUTE) {
                    popUpTo(SUBSCRIPTION_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = SUBSCRIPTION_COMPLETE_ROUTE) {
        SubscriptionCheckout {
            onSubscriptionComplete()
        }
    }
}