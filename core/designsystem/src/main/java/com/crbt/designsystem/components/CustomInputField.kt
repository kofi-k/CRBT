package com.crbt.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.extremelyDeemphasizedAlpha
import com.itengs.crbt.core.designsystem.R


enum class InputType {
    TEXT,
    PHONE_NUMBER,
    MONEY
}

enum class TextFieldType {
    OUTLINED,
    FILLED
}

@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    label: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    value: String,
    onClear: () -> Unit = {},
    enabled: Boolean = true,
    inputType: InputType = InputType.TEXT,
    showsErrors: Boolean = false,
    errorText: String = "",
    messageText: String = "",
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        errorTextColor = MaterialTheme.colorScheme.error,
        errorLeadingIconColor = MaterialTheme.colorScheme.error,
    ),
    shape: Shape = MaterialTheme.shapes.medium,
    textFieldType: TextFieldType = TextFieldType.FILLED,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
) {
    var showPassword by remember { mutableStateOf(false) }

    when (textFieldType) {
        TextFieldType.FILLED -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(text = label)
                },
                leadingIcon = leadingIcon,
                modifier = modifier,
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                enabled = enabled,
                colors = colors,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = value.isNotEmpty() && enabled,
                        enter = scaleIn(),
                        exit = scaleOut(),
                    ) {
                        IconButton(onClick = onClear) {
                            Icon(
                                imageVector = CrbtIcons.Clear,
                                contentDescription = CrbtIcons.Clear.name,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                },
                placeholder = {
                    if (inputType == InputType.PHONE_NUMBER) {
                        Text(
                            text = stringResource(id = R.string.core_designsystem_phone_number_placeholder),
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = extremelyDeemphasizedAlpha,
                            ),
                        )
                    } else {
                        Unit
                    }
                },
                visualTransformation = VisualTransformation.None,
                supportingText = {
                    TextFieldError(
                        textError = errorText,
                        isError = showsErrors,
                        messageText = messageText,
                    )
                },
                isError = showsErrors && enabled,
                shape = shape,
                maxLines = maxLines,
                minLines = minLines
            )
        }

        TextFieldType.OUTLINED -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(text = label)
                },
                leadingIcon = leadingIcon,
                modifier = modifier,
                singleLine = true,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                enabled = enabled,
                colors = colors,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = value.isNotEmpty() && enabled,
                        enter = scaleIn(),
                        exit = scaleOut(),
                    ) {
                        IconButton(onClick = onClear) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "clear text",
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                },
                placeholder = {
                    if (inputType == InputType.PHONE_NUMBER) {
                        Text(
                            text = stringResource(id = R.string.core_designsystem_phone_number_placeholder),
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = extremelyDeemphasizedAlpha,
                            ),
                        )
                    } else {
                        Unit
                    }
                },
                visualTransformation = VisualTransformation.None,
                supportingText = {
                    TextFieldError(
                        textError = errorText,
                        isError = showsErrors,
                        messageText = messageText,
                    )
                },
                isError = showsErrors && enabled,
                shape = shape,
            )
        }
    }
}