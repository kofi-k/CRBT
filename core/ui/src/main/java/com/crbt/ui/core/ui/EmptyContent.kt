package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.example.crbtjetcompose.core.ui.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    description: String,
    reloadContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {
        Text(
            text = stringResource(id = R.string.core_ui_no_content_text),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(slightlyDeemphasizedAlpha),
            ),
            fontWeight = FontWeight.Black,
        )
    },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        errorContent()
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            modifier = Modifier
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
        reloadContent()
    }

}