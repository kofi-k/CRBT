package com.crbt.subscription.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crbt.data.core.data.MusicControllerUiState
import com.crbt.subscription.CrbtSubscribeScreen
import com.crbt.subscription.SubscriptionCheckout
import com.crbt.subscription.TonesScreen

const val TONE_ID_ARG = "tone_id"
const val GIFT_SUB_ARG = "gift_sub"

const val SUBSCRIPTION_ROUTE = "subscriptions_route/$TONE_ID_ARG={$TONE_ID_ARG}"
const val SUBSCRIPTION_COMPLETE_ROUTE = "$SUBSCRIPTION_ROUTE/subscription_complete"


const val ADD_SUBSCRIPTION_ROUTE =
    "$SUBSCRIPTION_ROUTE/add_subscription?$TONE_ID_ARG={$TONE_ID_ARG}&$GIFT_SUB_ARG={$GIFT_SUB_ARG}"

fun NavController.navigateToAddSubscription(
    toneId: String,
    giftSub: Boolean,
    navOptions: NavOptions? = null
) {
    val route = "$SUBSCRIPTION_ROUTE/add_subscription?$TONE_ID_ARG=$toneId&$GIFT_SUB_ARG=$giftSub"
    navigate(route, navOptions)
}

fun NavController.navigateToSubscription(navOptions: NavOptions, toneId: String = "") =
    navigate(
        "subscriptions_route/$TONE_ID_ARG=$toneId",
        navOptions
    )


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.subscriptionScreen(
    navController: NavController,
    musicControllerUiState: MusicControllerUiState
) {
    composable(
        route = SUBSCRIPTION_ROUTE,
        arguments = listOf(navArgument(TONE_ID_ARG) { type = NavType.StringType })
    ) {

        TonesScreen(
            onSubscriptionToneClick = { toneId ->
                navController.navigateToAddSubscription(toneId, false)
            },
            onGiftSubscriptionClick = { toneId ->
                navController.navigateToAddSubscription(toneId, true)
            },
            musicControllerUiState = musicControllerUiState,
        )
    }

    composable(
        route = ADD_SUBSCRIPTION_ROUTE,
        arguments = listOf(
            navArgument(TONE_ID_ARG) { type = NavType.StringType },
            navArgument(GIFT_SUB_ARG) { type = NavType.BoolType }
        )
    ) {
        CrbtSubscribeScreen(
            onSubscribeClick = {
                navController.navigate(SUBSCRIPTION_COMPLETE_ROUTE)
            },
            onBackClicked = {
                navController.navigateUp()
            },
        )
    }

    composable(route = SUBSCRIPTION_COMPLETE_ROUTE) {
        SubscriptionCheckout(
            onDoneClicked = {
                navController.navigate(SUBSCRIPTION_ROUTE) {
                    popUpTo(SUBSCRIPTION_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
    }
}