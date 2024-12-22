package com.crbt.ui.core.ui.validationStates

class EmailValidationState(
    initialText: String = "",
) : GenericTextFieldState<String>(
    validator = {
        it.isValidEmail()
    },
    errorFor = {
        "Invalid email $it"
    },
    initialText = initialText,
)


fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}