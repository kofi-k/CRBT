package com.crbt.ui.core.ui.validationStates

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.rejowan.ccpc.Country

fun isValidPhoneNumber(
    phoneNumber: String,
    countryCode: String = Country.Ethiopia.countryCode,
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