package com.crbt.data.core.data.util

// name pattern: no numbers, no special characters, min 3
const val NAME_PATTERN = "^[a-zA-Z ]{3,}\$"
fun isValidName(name: String): Boolean {
    return NAME_PATTERN.toRegex().matches(name)
}

// error message for name
fun nameValidationError(name: String): String {
    if (!name.matches(Regex(NAME_PATTERN))) {
        val errorMessage = StringBuilder()

        if (name.contains("[0-9]".toRegex())) {
            errorMessage.append("- Should not include any numbers\n")
        }
        if (name.contains("[^A-Za-z ]".toRegex())) {
            errorMessage.append("- Should not include any special characters\n")
        }
        if (name.length < 3) {
            errorMessage.append("- Be at least 3 characters long\n")
        }

        return errorMessage.toString()
    }
    return ""
}