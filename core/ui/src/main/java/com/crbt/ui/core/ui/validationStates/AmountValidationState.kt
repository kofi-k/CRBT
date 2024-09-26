package com.crbt.ui.core.ui.validationStates

class AmountValidationState : GenericTextFieldState<String>(
    validator = {
        it.toDoubleOrNull() != null && it.toDouble() > 0 && it.length <= 4
    },
    errorFor = {
        "Invalid amount"
    },
    initialText = "",
)