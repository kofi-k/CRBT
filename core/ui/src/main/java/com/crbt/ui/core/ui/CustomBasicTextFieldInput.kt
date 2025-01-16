package com.crbt.ui.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.itengs.crbt.core.designsystem.R

@Composable
fun CustomBasicTextFieldInput(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    placeholder: String = stringResource(id = R.string.core_designsystem_phone_number_placeholder),
    onClear: () -> Unit = {},
    onDone: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
    hasError: Boolean = false,
    enabled: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {
            focusManager.clearFocus()
            onDone()
        },
    ),
    singleLine: Boolean = true,
    minLines: Int = 1,
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
        label = "value_color_anim",
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
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
                        .padding(TextFieldDefaults.contentPaddingWithLabel()),
                    verticalArrangement = Arrangement.Center,
                ) {
                    when (value.isEmpty() && !hasFocus) {
                        true -> Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = stronglyDeemphasizedAlpha,
                            ),
                        )

                        false -> innerTextField()
                    }
                }
            },
            cursorBrush = SolidColor(color),
        )
        Spacer(modifier = Modifier.width(8.dp))
        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.padding(top = 4.dp, start = 16.dp)
        ) {
            IconButton(
                onClick = onClear,
                enabled = enabled,
            ) {
                Icon(
                    imageVector = CrbtIcons.Clear,
                    contentDescription = CrbtIcons.Clear.name,
                    modifier = Modifier.size(24.dp),
                    tint = color,
                )
            }
        }
    }
}