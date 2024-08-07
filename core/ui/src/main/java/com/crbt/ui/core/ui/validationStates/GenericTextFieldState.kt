package com.crbt.ui.core.ui.validationStates

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * A generic state holder for a text field that holds the text
 * and its validation state.
 * @param T the type of the text
 * @param validator the validation function for the text
 * @param errorFor the error message provider for the text
 * @param initialText the initial text for the text field
 * */
open class GenericTextFieldState<T>(
    private val validator: (T) -> Boolean = { false },
    private val errorFor: (T) -> String = { "" },
    initialText: T,
) {
    var text: T by mutableStateOf(initialText)

    private var isFocusedDirty: Boolean by mutableStateOf(false)
    private var isFocused: Boolean by mutableStateOf(false)
    private var displayErrors: Boolean by mutableStateOf(false)

    open val isValid: Boolean
        get() = validator(text)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun enableShowErrors() {
        // only show errors if the text was at least once focused
        if (isFocusedDirty) {
            displayErrors = true
        }
    }

    // reset the state of the text field
    fun reset() {
        isFocusedDirty = false
        isFocused = false
        displayErrors = false
    }

    fun showErrors() = !isValid && displayErrors

    open fun getError(): String? {
        return if (showErrors()) {
            errorFor(text)
        } else {
            null
        }
    }
}