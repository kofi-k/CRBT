package com.crbt.ui.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.TextFieldType
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.validationStates.EmailValidationState
import com.itengs.crbt.core.ui.R

@Composable
fun EmailCheck(
    modifier: Modifier = Modifier,
    onEmailCheckChanged: (Boolean) -> Unit,
    checked: Boolean,
    userEmailAddress: String,
    onEmailChanged: (String, Boolean) -> Unit,
) {

    val email by remember {
        mutableStateOf(EmailValidationState(initialText = userEmailAddress))
    }

    Column(modifier = Modifier) {
        Row(
            modifier = modifier
                .clickable { onEmailCheckChanged(!checked) },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onEmailCheckChanged,
            )
            Text(
                text = stringResource(id = R.string.core_ui_email_title),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        if (checked) {
            val focusManager = LocalFocusManager.current

            CustomInputField(
                label = stringResource(id = R.string.core_ui_email_label),
                value = email.text,
                onValueChange = {
                    email.text = it
                    onEmailChanged(email.text, email.isValid)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        email.onFocusChange(focusState.isFocused)
                        if (!focusState.isFocused) {
                            email.enableShowErrors()
                        }
                    },
                inputType = InputType.TEXT,
                onClear = {
                    email.text = ""
                    onEmailChanged(email.text, email.isValid)
                },
                leadingIcon = {
                    Icon(
                        imageVector = CrbtIcons.Email,
                        contentDescription = CrbtIcons.Email.name,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        email.enableShowErrors()
                        focusManager.clearFocus()
                    },
                ),
                showsErrors = email.showErrors(),
                errorText = email.getError() ?: "",
                colors = OutlinedTextFieldDefaults.colors(),
                textFieldType = TextFieldType.OUTLINED
            )
        }
    }
}