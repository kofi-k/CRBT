package com.crbt.subscription.navigation

import android.annotation.SuppressLint
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
import com.crbt.subscription.TonesScreen
import com.crbt.ui.core.ui.musicPlayer.CrbtTonesViewModel

const val TONE_ID_ARG = "tone_id"
const val GIFT_SUB_ARG = "gift_sub"

const val SUBSCRIPTION_ROUTE = "subscriptions_route/$TONE_ID_ARG={$TONE_ID_ARG}"


const val ADD_SUBSCRIPTION_ROUTE =
    "$SUBSCRIPTION_ROUTE/add_subscription?$TONE_ID_ARG={$TONE_ID_ARG}&$GIFT_SUB_ARG={$GIFT_SUB_ARG}"

fun NavController.navigateToAddSubscription(
    toneId: String,
    giftSub: Boolean,
) {
    val route = "$SUBSCRIPTION_ROUTE/add_subscription?$TONE_ID_ARG=$toneId&$GIFT_SUB_ARG=$giftSub"
    navigate(route) {
        if (currentDestination?.route != SUBSCRIPTION_ROUTE) {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.navigateToSubscription(navOptions: NavOptions, toneId: String = "") =
    navigate(
        "subscriptions_route/$TONE_ID_ARG=$toneId",
        navOptions
    )


@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.subscriptionScreen(
    navController: NavController,
    crbtTonesViewModel: CrbtTonesViewModel,
    musicControllerUiState: MusicControllerUiState,
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
            crbtTonesViewModel = crbtTonesViewModel,
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
            onSubscribeSuccess = {
                navController.navigateUp()
            },
            onBackClicked = {
                navController.navigateUp()
            },
            crbtTonesViewModel = crbtTonesViewModel,
            musicControllerUiState = musicControllerUiState,
        )
    }
}