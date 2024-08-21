package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha

@Composable
fun OnboardingSheetContainer(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable() (ColumnScope.() -> Unit) = { }
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text =  subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}