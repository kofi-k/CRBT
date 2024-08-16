package com.crbt.data.core.data

import androidx.annotation.StringRes
import com.example.crbtjetcompose.core.data.R


data class CRBTSettingsData(
    val id: String,
   @StringRes val name: Int,
) {
    companion object {
        val languages = listOf(
            CRBTSettingsData("1", R.string.core_data_amharic),
            CRBTSettingsData("2", R.string.core_data_oromiffa),
            CRBTSettingsData("3", R.string.core_data_tigrigna),
            CRBTSettingsData("4", R.string.core_data_somalia),
            CRBTSettingsData("5", R.string.core_data_arabic),
            CRBTSettingsData("6", R.string.core_data_english),
        )

        // camera, photos, microphone, storage, contacts, location, sms, ussd,
        val permissions = listOf(
            CRBTSettingsData("1", R.string.core_data_camera),
            CRBTSettingsData("2", R.string.core_data_photos),
            CRBTSettingsData("3", R.string.core_data_microphone),
            CRBTSettingsData("4", R.string.core_data_storage),
            CRBTSettingsData("5", R.string.core_data_contacts),
            CRBTSettingsData("6", R.string.core_data_location),
            CRBTSettingsData("7", R.string.core_data_sms),
            CRBTSettingsData("8", R.string.core_data_ussd),
        )
    }
}