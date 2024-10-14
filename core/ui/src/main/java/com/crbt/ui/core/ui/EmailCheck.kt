package com.crbt.ui.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.crbtjetcompose.core.ui.R

@Composable
fun EmailCheck(
    modifier: Modifier = Modifier,
    onEmailCheckChanged: (Boolean) -> Unit,
) {
    var checked by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .clickable {
                checked = !checked
                onEmailCheckChanged(checked)
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = !checked
                onEmailCheckChanged(checked)
            },
        )
        Text(
            text = stringResource(id = R.string.core_ui_email_label),
            style = MaterialTheme.typography.bodyMedium,
        )

    }
}