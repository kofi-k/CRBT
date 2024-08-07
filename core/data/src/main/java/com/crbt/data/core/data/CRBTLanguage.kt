package com.crbt.data.core.data

import androidx.annotation.StringRes
import com.example.crbtjetcompose.core.data.R

enum class  CRBTLanguage(
    @StringRes val languageResValue: Int,
) {
    AMHARIC(R.string.core_data_amharic),
    OROMIFFA(R.string.core_data_oromiffa),
    TIGRIGNA(R.string.core_data_tigrigna),
    SOMALIA(R.string.core_data_somalia),
    ARABIC(R.string.core_data_arabic),
    ENGLISH(R.string.core_data_english),
}