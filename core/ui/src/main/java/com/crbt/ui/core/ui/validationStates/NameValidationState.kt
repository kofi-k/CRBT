package com.crbt.ui.core.ui.validationStates

import com.crbt.data.core.data.util.isValidName
import com.crbt.data.core.data.util.nameValidationError

class NameValidationState : GenericTextFieldState<String>(
    validator = { isValidName(it) },
    errorFor = { nameValidationError(it) },
    initialText = "",
)


class AmountValidationState : GenericTextFieldState<String>(
    validator = {
        it.toDoubleOrNull() != null && it.toDouble() > 0 && it.length <= 4
    },
    errorFor = {
        "Invalid amount"
    },
    initialText = "",
)

