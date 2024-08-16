package com.crbt.data.core.data

import androidx.annotation.StringRes
import com.example.crbtjetcompose.core.data.R


enum class SubscriptionBillingType(
    @StringRes val title: Int
) {
    Monthly(title = R.string.core_data_monthly_bill_title),
    BiWeekly(title = R.string.core_data_bi_weekly_bill_title),
    Weekly(title = R.string.core_data_weekly_bill_title)
}


