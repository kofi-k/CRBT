package com.crbt.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.validationStates.NameValidationState
import com.example.crbtjetcompose.feature.onboarding.R

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onUserProfileResponse: (String, String, Boolean) -> Unit,
    onEmailCheckChanged: (Boolean) -> Unit,
    onSaveButtonClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(id = R.string.feature_onboarding_profile_setup_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        UsernameDetails(
            modifier = modifier,
            onUserProfileResponse = onUserProfileResponse,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingSheetContainer(
            titleRes = R.string.feature_onboarding_updates_title,
            subtitleRes = R.string.feature_onboarding_updates_subtitle,
            content = {
                EmailCheck(
                    modifier = modifier,
                    onEmailCheckChanged = onEmailCheckChanged,
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProcessButton(
            onClick = onSaveButtonClicked,
            modifier = modifier
                .fillMaxWidth(),
        )
    }

}

@Composable
internal fun EmailCheck(
    modifier: Modifier = Modifier,
    onEmailCheckChanged: (Boolean) -> Unit,
) {
    val checked by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onEmailCheckChanged(it)
            },
        )
        Text(
            text = stringResource(id = R.string.feature_onboarding_profile_email_label),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


@Composable
internal fun UsernameDetails(
    modifier: Modifier = Modifier,
    onUserProfileResponse: (String, String, Boolean) -> Unit,
) {
    val firstName by remember {
        mutableStateOf(NameValidationState())
    }
    val lastName by remember {
        mutableStateOf(NameValidationState())
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val focusManager = LocalFocusManager.current

        CustomInputField(
            label = R.string.feature_onboarding_profile_first_name_label,
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
            colors = TextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
//                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(
//                    stronglyDeemphasizedAlpha,
//                ),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorTextColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomInputField(
            label = R.string.feature_onboarding_profile_last_name_label,
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
            colors = TextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
//                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(
//                    stronglyDeemphasizedAlpha,
//                ),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorTextColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
            ),
            showsErrors = lastName.showErrors(),
            errorText = lastName.getError() ?: "",
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun UsernameDetailsPreview() {
    UsernameDetails(
        onUserProfileResponse = { _, _, _ -> },
    )
}

@Preview
@Composable
fun EmailCheckPreview() {
    EmailCheck(
        onEmailCheckChanged = {},
    )
}

@Preview
@Composable
fun ProfilePreview() {
    Profile(
        onUserProfileResponse = { _, _, _ -> },
        onEmailCheckChanged = {},
        onSaveButtonClicked = {},
    )
}