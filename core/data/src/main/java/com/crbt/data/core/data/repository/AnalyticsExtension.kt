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

internal fun AnalyticsHelper.logUserPreferedLanguage(languageCode: String) {
    logEvent(
        AnalyticsEvent(
            type = "user_preferred_language",
            extras = listOf(
                AnalyticsEvent.Param(key = "language_code", value = languageCode),
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


