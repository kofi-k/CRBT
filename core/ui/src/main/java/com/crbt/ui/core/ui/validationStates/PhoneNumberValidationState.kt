package com.crbt.ui.core.ui.validationStates

import com.rejowan.ccpc.Country

class PhoneNumberValidationState(
    countryCode: String = Country.Ethiopia.countryCode
) : GenericTextFieldState<String>(
    validator = {
        isValidPhoneNumber(
            phoneNumber = "$countryCode${it.removeCountryCodeIfPresent()}",
            countryCode = countryCode
        )
    },
    errorFor = { "Invalid phone number" },
    initialText = "",
)

fun String.removeCountryCodeIfPresent(
    countryCode: String = Country.Ethiopia.countryCode
): String =
    if (startsWith(countryCode)) {
        substring(countryCode.length)
    } else {
        this
    }