package com.crbt.data.core.data.repository

import com.example.crbtjetcompose.core.analytics.AnalyticsEvent
import com.example.crbtjetcompose.core.analytics.AnalyticsHelper


internal fun AnalyticsHelper.logGiftedCrbtToneSubscription(toneId: String, phoneNumber: String) {
    logEvent(
        AnalyticsEvent(
            type = "gifted_crbt_tone_subscription",
            extras = listOf(
                AnalyticsEvent.Param(key = "tone_id", value = toneId),
                AnalyticsEvent.Param(key = "phone_number", value = phoneNumber),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logCrbtToneSubscription(toneId: String) {
    logEvent(
        AnalyticsEvent(
            type = "crbt_tone_subscription",
            extras = listOf(
                AnalyticsEvent.Param(key = "tone_id", value = toneId),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logCrbtToneUnsubscription(toneId: String) {
    logEvent(
        AnalyticsEvent(
            type = "crbt_tone_unsubscription",
            extras = listOf(
                AnalyticsEvent.Param(key = "tone_id", value = toneId),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logUserDetails(
    firstName: String,
    lastName: String,
    phoneNumber: String,
    langPref: String,
) {
    logEvent(
        AnalyticsEvent(
            type = "user_info_update",
            extras = listOf(
                AnalyticsEvent.Param(key = "first_name", value = firstName),
                AnalyticsEvent.Param(key = "last_name", value = lastName),
                AnalyticsEvent.Param(key = "phone_number", value = phoneNumber),
                AnalyticsEvent.Param(key = "lang_pref", value = langPref),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logUserPreferedCurrency(currency: String) {
    logEvent(
        AnalyticsEvent(
            type = "user_preferred_currency",
            extras = listOf(
                AnalyticsEvent.Param(key = "currency", value = currency),
            ),
        ),
    )
}


