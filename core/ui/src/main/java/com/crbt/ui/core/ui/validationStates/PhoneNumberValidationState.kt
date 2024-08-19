package com.crbt.ui.core.ui.validationStates

class PhoneNumberValidationState(
    countryCode: String,
) : GenericTextFieldState<String>(
    validator = {
        isValidPhoneNumber(
            phoneNumber = "$countryCode$it",
            countryCode = countryCode
        )
    },
    errorFor = { "Invalid phone number" },
    initialText = "",
)

