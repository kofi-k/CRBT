package com.crbt.ui.core.ui.validationStates

import com.crbt.data.core.data.util.isValidName
import com.crbt.data.core.data.util.nameValidationError

class NameValidationState(
    initialText: String = "",
) : GenericTextFieldState<String>(
    validator = { isValidName(it) },
    errorFor = { nameValidationError(it) },
    initialText = initialText,
)