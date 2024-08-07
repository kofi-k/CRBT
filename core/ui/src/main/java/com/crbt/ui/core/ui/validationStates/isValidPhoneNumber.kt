package com.crbt.ui.core.ui.validationStates

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

fun isValidPhoneNumber(
        phoneNumber: String,
        countryCode: String = "US",
    ): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            val numberProto: Phonenumber.PhoneNumber =
                phoneUtil.parse(
                    phoneNumber,
                    countryCode,
                )
            phoneUtil.isValidNumber(numberProto)
        } catch (e: NumberParseException) {
            false
        }
    }