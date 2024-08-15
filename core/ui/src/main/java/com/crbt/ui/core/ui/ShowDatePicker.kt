package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ShowDatePicker(
    title: String,
    datePickerFormatter: DatePickerFormatter = remember {
        DatePickerDefaults.dateFormatter()
    },
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            datePickerState.selectedDateMillis?.let { onDateSelected(it) }
        },
    ) {
        DatePicker(
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(16.dp),
                )
            },
            state = datePickerState,
            dateFormatter = datePickerFormatter,
        )
    }
}