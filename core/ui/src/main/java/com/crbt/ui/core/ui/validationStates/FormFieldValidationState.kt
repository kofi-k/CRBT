package com.crbt.ui.core.ui.validationStates

class FormFieldValidationState(
    initialText: String = "",
    type: String
) : GenericTextFieldState<String>(
    validator = { text ->
        text.isNotEmpty() && text.first().isLetter()
    },
    errorFor = { text ->
        when {
            text.isEmpty() -> "$type is required"
            !text.first().isLetter() -> "First character should be a letter"
            else -> "Invalid $type"
        }
    },
    initialText = initialText,
)