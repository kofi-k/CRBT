package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import com.crbt.ui.core.ui.validationStates.NameValidationState
import com.example.crbtjetcompose.core.ui.R

@Composable
fun UsernameDetails(
    modifier: Modifier = Modifier,
    onUserProfileResponse: (String, String, Boolean) -> Unit,
    initialFirstName: String,
    initialLastName: String,
) {
    val firstName by remember {
        mutableStateOf(NameValidationState(initialText = initialFirstName))
    }
    val lastName by remember {
        mutableStateOf(NameValidationState(initialText = initialLastName))
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val focusManager = LocalFocusManager.current

        CustomInputField(
            label = stringResource(id = R.string.core_ui_first_name_label),
            value = firstName.text,
            onValueChange = {
                firstName.text = it
                onUserProfileResponse(
                    firstName.text,
                    lastName.text,
                    firstName.isValid && lastName.isValid,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    firstName.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        firstName.enableShowErrors()
                    }
                },
            inputType = InputType.TEXT,
            onClear = {
                firstName.text = ""
                onUserProfileResponse(
                    firstName.text,
                    lastName.text,
                    firstName.isValid && lastName.isValid,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = CrbtIcons.Person,
                    contentDescription = CrbtIcons.Person.name,
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    firstName.enableShowErrors()
                    focusManager.moveFocus(FocusDirection.Down)
                },
            ),
            showsErrors = lastName.showErrors(),
            errorText = firstName.getError() ?: "",
            colors = OutlinedTextFieldDefaults.colors(),
            textFieldType = TextFieldType.OUTLINED
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomInputField(
            label = stringResource(id = R.string.core_ui_last_name_label),
            value = lastName.text,
            onValueChange = {
                lastName.text = it
                onUserProfileResponse(
                    firstName.text,
                    lastName.text,
                    firstName.isValid && lastName.isValid,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    lastName.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        lastName.enableShowErrors()
                    }
                },
            inputType = InputType.TEXT,
            onClear = {
                lastName.text = ""
                onUserProfileResponse(
                    firstName.text,
                    lastName.text,
                    firstName.isValid && lastName.isValid,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = CrbtIcons.Person,
                    contentDescription = CrbtIcons.Person.name,
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    lastName.enableShowErrors()
                },
            ),
            colors = OutlinedTextFieldDefaults.colors(),
            textFieldType = TextFieldType.OUTLINED,
            showsErrors = lastName.showErrors(),
            errorText = lastName.getError() ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}