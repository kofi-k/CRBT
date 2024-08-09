package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha

@Composable
fun CustomInputButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    leadingIcon: ImageVector = CrbtIcons.Language,
    trailingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = CrbtIcons.ArrowRight,
            contentDescription = CrbtIcons.ArrowRight.name,
        )
    },
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.outlineVariant.copy(
            stronglyDeemphasizedAlpha,
        ),
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
) {
    Button(
        onClick = onClick,
        colors = colors,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation =elevation
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = leadingIcon.name,
        )
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 12.dp),
        )
        trailingIcon()
    }
}