package com.crbt.data.core.data

import androidx.annotation.StringRes
import com.example.crbtjetcompose.core.data.R

data class CrbtRegistrationPackage(
    val id: Int,
    @StringRes val duration: Int,
    val price: Int,
) {
    companion object {
        val packages = listOf(
            CrbtRegistrationPackage(1, R.string.core_data_monthly, 7),
            CrbtRegistrationPackage(2, R.string.core_data_bi_weekly, 4),
            CrbtRegistrationPackage(3, R.string.core_data_weekly, 3)
        )
    }
}