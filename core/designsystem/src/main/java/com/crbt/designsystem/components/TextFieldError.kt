package com.crbt.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldError(
    textError: String,
    modifier: Modifier = Modifier,
    isError: Boolean,
    messageText: String,
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.width(16.dp))
        AnimatedContent(
            targetState = isError,
            label = "amount validation animation",
        ) { isError ->
            when (isError) {
                true -> Text(
                    text = textError,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.error,
                )

                false -> Text(
                    text = messageText,
                    modifier = Modifier,
                )
            }
        }
    }
}