package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.designsystem.icon.CrbtIcons
import com.itengs.crbt.core.ui.R
import com.itengs.crbt.core.designsystem.R as desR

@Composable
fun UssdResponseDialog(
    onDismiss: () -> Unit,
    ussdUiState: UssdUiState,
    crbtUssdType: CrbtUssdType
) {
    val message = getUssdUiStateMessage(ussdUiState, crbtUssdType)

    AlertDialog(
        title = {
            Text(
                text = when (message) {
                    is UssdUiStateMessage.Error -> message.title
                    is UssdUiStateMessage.Success -> message.title
                    is UssdUiStateMessage.Untitled -> stringResource(id = desR.string.core_designsystem_untitled)
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        },
        text = {
            Text(
                text = when (message) {
                    is UssdUiStateMessage.Error -> message.message
                    is UssdUiStateMessage.Success -> message.message
                    is UssdUiStateMessage.Untitled -> stringResource(id = desR.string.core_designsystem_untitled)
                }
            )
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


sealed class UssdUiStateMessage {
    data class Error(val title: String, val message: String) : UssdUiStateMessage()
    data class Success(val title: String, val message: String) : UssdUiStateMessage()
    data object Untitled : UssdUiStateMessage()
}


@Composable
fun getUssdUiStateMessage(
    ussdUiState: UssdUiState,
    crbtUssdType: CrbtUssdType
): UssdUiStateMessage {
    return when (ussdUiState) {
        is UssdUiState.Error -> UssdUiStateMessage.Error(
            title = when (crbtUssdType) {
                CrbtUssdType.BALANCE_CHECK -> stringResource(id = R.string.core_ui_error_check_balance)
                CrbtUssdType.CALL_ME_BACK -> stringResource(id = R.string.core_ui_call_me_back_error)
                CrbtUssdType.RECHARGE -> stringResource(id = R.string.core_ui_recharge_error)
                CrbtUssdType.TRANSFER -> stringResource(id = R.string.core_ui_transfer_error)
                CrbtUssdType.CRBT_SUBSCRIBE -> stringResource(id = R.string.core_ui_subscription_error)
                CrbtUssdType.PACKAGE_SUBSCRIBE -> stringResource(id = R.string.core_ui_package_subscription_error)
            },
            message = ussdUiState.error
        )

        is UssdUiState.Success -> UssdUiStateMessage.Success(
            title = when (crbtUssdType) {
                CrbtUssdType.BALANCE_CHECK -> stringResource(id = R.string.core_ui_balance_check)
                CrbtUssdType.CALL_ME_BACK -> stringResource(id = R.string.core_ui_call_me_back_success)
                CrbtUssdType.RECHARGE -> stringResource(id = R.string.core_ui_recharge_success)
                CrbtUssdType.TRANSFER -> stringResource(id = R.string.core_ui_transfer_success)
                CrbtUssdType.CRBT_SUBSCRIBE -> stringResource(id = R.string.core_ui_subscription_success)
                CrbtUssdType.PACKAGE_SUBSCRIBE -> stringResource(id = R.string.core_ui_package_subscription_success)
            },
            message = ussdUiState.response
        )

        else -> UssdUiStateMessage.Untitled
    }
}


// preview dialog
@Preview
@Composable
fun BalanceDialogPreview() {
    UssdResponseDialog(
        onDismiss = { },
        ussdUiState = UssdUiState.Success("100"),
        crbtUssdType = CrbtUssdType.BALANCE_CHECK
    )
}