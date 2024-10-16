package com.crbt.data.core.data.repository


sealed class UssdUiState {
    data object Idle : UssdUiState()
    data object Loading : UssdUiState()
    data class Success(val response: String) : UssdUiState()
    data class Error(val error: String) : UssdUiState()
}

const val EXTRACTION_BY_WORD = "BIRR"

fun String.extractBalance(word: String = EXTRACTION_BY_WORD): Double? {
    val regex = Regex("""(\d+(\.\d+)?)\s*$word""", RegexOption.IGNORE_CASE)
    val match = regex.find(this)

    return match?.groupValues?.get(1)?.toDoubleOrNull()
}