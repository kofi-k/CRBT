package com.crbt.ui.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.validationStates.PhoneNumberValidationState
import com.rejowan.ccpc.CCPUtils.Companion.getEmojiFlag
import com.rejowan.ccpc.Country
import com.example.crbtjetcompose.core.ui.R as CoreUiR

@Composable
fun PhoneEntryScreen(
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    enabled: Boolean,
) {

    val countryCode by remember {
        mutableStateOf(Country.Ethiopia.countryCode)
    }

    val phoneNumberState by remember {
        mutableStateOf(
            PhoneNumberValidationState(
                countryCode = countryCode,
            ),
        )
    }


    PhoneEntryTextField(
        onPhoneNumberChanged = {
            phoneNumberState.text = it
            onPhoneNumberChanged("$countryCode$it", phoneNumberState.isValid)
        },
        phoneNumber = phoneNumberState.text,
        onClear = {
            phoneNumberState.text = ""
            onPhoneNumberChanged("", false)
        },
        onDone = { phoneNumberState.enableShowErrors() },
        showsErrors = phoneNumberState.showErrors(),
        enabled = enabled
    )
    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    onPhoneNumberChanged: (String) -> Unit,
    phoneNumber: String,
    onClear: () -> Unit = {},
    onDone: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    hasError: Boolean = false,
    enabled: Boolean,
) {
    var hasFocus by remember {
        mutableStateOf(false)
    }
    val color by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "phone_number_text_color",
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChanged,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                },
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = color,
            ),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        hasFocus = true
                    }
                },
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    when (phoneNumber.isEmpty() && !hasFocus) {
                        true -> Text(
                            text = stringResource(id = CoreUiR.string.core_ui_phone_number_placeholder),
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = stronglyDeemphasizedAlpha,
                            ),
                        )

                        false -> innerTextField()
                    }
                }
            },
        )
        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(
            visible = phoneNumber.isNotEmpty(),
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            IconButton(
                onClick = onClear,
                enabled = enabled,
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "clear text",
                    modifier = Modifier.size(24.dp),
                    tint = color,
                )
            }
        }
    }
}

@Composable
fun PhoneEntryTextField(
    modifier: Modifier = Modifier,
    onPhoneNumberChanged: (String) -> Unit,
    phoneNumber: String,
    onClear: () -> Unit = {},
    onDone: () -> Unit = {},
    showsErrors: Boolean = false,
    focusManager: FocusManager = LocalFocusManager.current,
    enabled: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            val background by animateColorAsState(
                targetValue = if (showsErrors) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.outlineVariant.copy(
                        stronglyDeemphasizedAlpha,
                    )
                },
                label = "phone_number_border_color",
            )

            CountryCodeChip(country = Country.Ethiopia)

            PhoneNumberInput(
                onPhoneNumberChanged = onPhoneNumberChanged,
                phoneNumber = phoneNumber,
                onClear = onClear,
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                },
                hasError = showsErrors,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = background,
                        shape = MaterialTheme.shapes.medium,
                    ),
                focusManager = focusManager,
                enabled = enabled
            )
        }
        AnimatedVisibility(
            visible = showsErrors,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
            modifier = Modifier.padding(end = 8.dp),
        ) {
            Text(
                text = stringResource(id = CoreUiR.string.core_ui_phone_number_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun CountryCodeChip(
    country: Country
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    stronglyDeemphasizedAlpha,
                ),
                shape = MaterialTheme.shapes.medium,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = getEmojiFlag(country.countryIso),
            modifier = Modifier.padding(start = 16.dp, end = 10.dp),
            style = MaterialTheme.typography.bodyLarge
        )


        Text(
            text = country.countryCode,
            modifier = Modifier.padding(end = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun CountryCodeChipPreview() {
    CrbtTheme {
        CountryCodeChip(
            country = Country.Ethiopia
        )
    }
}

@Preview
@Composable
fun PhoneEntryTextFieldPreview() {
    CrbtTheme {
        PhoneEntryTextField(
            onPhoneNumberChanged = {},
            phoneNumber = "3468349",
            onClear = {},
            onDone = {},
            showsErrors = false,
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneEntryScreenPreview() {
    CrbtTheme {
        PhoneEntryScreen(
            onPhoneNumberChanged = { _, _ -> },
            enabled = true
        )
    }
}
