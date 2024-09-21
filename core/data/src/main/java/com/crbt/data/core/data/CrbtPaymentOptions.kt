package com.crbt.data.core.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.crbtjetcompose.core.data.R

enum class CrbtPaymentOptions(
    val id: Int,
    @StringRes val title: Int,
    @DrawableRes val imageRes: Int
) {
    CHAPA(
        id = 1,
        title = R.string.core_data_chapa,
        imageRes = R.drawable.chapa
    ),
    SANTIM_PAY(
        id = 2,
        title = R.string.core_data_santim_pay,
        imageRes = R.drawable.santim
    ),
    TELEBIR(
        id = 3,
        title = R.string.core_data_telebir,
        imageRes = R.drawable.telebir
    )

}