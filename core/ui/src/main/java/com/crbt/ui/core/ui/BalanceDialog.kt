package com.crbt.ui.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.core.ui.R
import com.example.crbtjetcompose.core.designsystem.R as desR

@Composable
fun BalanceDialog(
    onDismiss: () -> Unit,
    ussdUiState: UssdUiState
) {
    val message = when (ussdUiState) {
        is UssdUiState.Error -> stringResource(id = R.string.core_ui_error_check_balance) to ussdUiState.error.toString()
        is UssdUiState.Success -> stringResource(id = R.string.core_ui_balance_check) to ussdUiState.response
        else -> stringResource(id = desR.string.core_designsystem_untitled) to
                stringResource(id = desR.string.core_designsystem_untitled)
    }
    AlertDialog(
        title = {
            Text(text = message.first)
        },
        text = {
            Text(text = message.second)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.core_ui_ok))
            }
        },
        icon = {
            when (ussdUiState) {
                is UssdUiState.Error -> Icon(
                    imageVector = CrbtIcons.Close,
                    contentDescription = null
                )

                is UssdUiState.Success -> Icon(
                    imageVector = CrbtIcons.Check,
                    contentDescription = null
                )

                else -> Unit
            }
        }
    )
}


// preview dialog
@Preview
@Composable
fun BalanceDialogPreview() {
    BalanceDialog(
        onDismiss = { },
        ussdUiState = UssdUiState.Success("100")
    )
}