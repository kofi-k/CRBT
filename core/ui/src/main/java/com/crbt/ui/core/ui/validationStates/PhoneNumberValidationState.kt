package com.crbt.ui.core.ui.validationStates

class PhoneNumberValidationState(
    countryCode: String,
) : GenericTextFieldState<String>(
    validator = { isValidPhoneNumber(it, countryCode) },
    errorFor = { "Invalid phone number" },
    initialText = "",
)

