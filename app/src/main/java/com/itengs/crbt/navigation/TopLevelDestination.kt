package com.itengs.crbt.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.crbt.designsystem.icon.CrbtIcons
import com.itengs.crbt.feature.home.R as homeR
import com.itengs.crbt.feature.profile.R as profileR
import com.itengs.crbt.feature.services.R as servicesR
import com.itengs.crbt.feature.subscription.R as subscriptionR

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens. Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = CrbtIcons.Home,
        unselectedIcon = CrbtIcons.HomeBorder,
        iconTextId = homeR.string.feature_home_title,
        titleTextId = homeR.string.feature_home_title,
    ),
    SERVICES(
        selectedIcon = CrbtIcons.Services,
        unselectedIcon = CrbtIcons.ServicesBorder,
        iconTextId = servicesR.string.feature_services_title,
        titleTextId = servicesR.string.feature_services_title,
    ),
    SUBSCRIPTIONS(
        selectedIcon = CrbtIcons.Subscription,
        unselectedIcon = CrbtIcons.SubscriptionBorder,
        iconTextId = subscriptionR.string.feature_subscription_title,
        titleTextId = subscriptionR.string.feature_subscription_title,
    ),
    PROFILE(
        selectedIcon = CrbtIcons.PersonSettings,
        unselectedIcon = CrbtIcons.PersonSettingsBorder,
        iconTextId = profileR.string.feature_profile_title,
        titleTextId = profileR.string.feature_profile_title,
    ),
}