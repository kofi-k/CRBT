package com.crbt.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.TextFieldType
import com.crbt.services.packages.ButtonActionRow
import com.crbt.ui.core.ui.GiftPurchasePhoneNumber
import com.crbt.ui.core.ui.validationStates.AmountValidationState
import com.example.crbtjetcompose.feature.services.R

enum class ServicesType {
    CALL_ME_BACK,
    TRANSFER,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesBottomSheet(
    servicesType: ServicesType,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirmCallMeBack: (String) -> Unit,
    onConfirmTransfer: (String, Double) -> Unit,
    actionLoading: Boolean,
) {
    val title = when (servicesType) {
        ServicesType.CALL_ME_BACK ->
            stringResource(id = R.string.feature_services_call_back) to
                    stringResource(id = R.string.feature_services_call_back_button)

        ServicesType.TRANSFER -> stringResource(id = R.string.feature_services_transfer) to
                stringResource(id = R.string.feature_services_transfer)
    }

    ServiceSheetContainer(
        title = title.first,
        content = {
            ServicesSheetContent(
                servicesType = servicesType,
                onConfirmCallMeBack = onConfirmCallMeBack,
                onConfirmTransfer = onConfirmTransfer,
                actionLoading = actionLoading,
                onDismiss = onDismiss
            )
        },
        sheetState = sheetState,
        onDismiss = onDismiss,
    )
}


@Composable
fun ServicesSheetContent(
    servicesType: ServicesType,
    onConfirmCallMeBack: (String) -> Unit,
    onConfirmTransfer: (String, Double) -> Unit,
    onDismiss: () -> Unit,
    actionLoading: Boolean,
) {
    when (servicesType) {
        ServicesType.CALL_ME_BACK -> {
            CallMeBackContent(
                modifier = Modifier,
                onConfirmCallMeBack = onConfirmCallMeBack,
                onDismiss = onDismiss,
                actionLoading = actionLoading,
            )
        }

        ServicesType.TRANSFER -> {
            TransferContent(
                modifier = Modifier,
                onConfirmTransfer = onConfirmTransfer,
                onDismiss = onDismiss,
                actionLoading = actionLoading,
            )
        }
    }
}


@Composable
fun CallMeBackContent(
    modifier: Modifier = Modifier,
    onConfirmCallMeBack: (String) -> Unit,
    onDismiss: () -> Unit,
    actionLoading: Boolean,
) {
    var userPhoneNumber by remember {
        mutableStateOf(
            "" to false
        )
    }
    Column(
        modifier = modifier
    ) {
        GiftPurchasePhoneNumber(
            onPhoneNumberChanged = { phoneNumber, isValid ->
                userPhoneNumber = phoneNumber to isValid
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonActionRow(
            onConfirmClick = { onConfirmCallMeBack(userPhoneNumber.first) },
            onDismissClick = onDismiss,
            actionText = stringResource(id = R.string.feature_services_call_back_button),
            actionLoading = actionLoading,
            actionEnabled = userPhoneNumber.second,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TransferContent(
    modifier: Modifier,
    onConfirmTransfer: (String, Double) -> Unit,
    onDismiss: () -> Unit,
    actionLoading: Boolean,
) {
    val focusManager = LocalFocusManager.current
    val amountState by remember {
        mutableStateOf(AmountValidationState())
    }
    var userPhoneNumber by remember {
        mutableStateOf(
            "" to false
        )
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        GiftPurchasePhoneNumber(
            onPhoneNumberChanged = { phoneNumber, isValid ->
                userPhoneNumber = phoneNumber to isValid
            },
            modifier = Modifier.fillMaxWidth()
        )
        CustomInputField(
            onValueChange = {
                amountState.text = it
            },
            value = amountState.text,
            inputType = InputType.MONEY,
            textFieldType = TextFieldType.OUTLINED,
            label = stringResource(id = com.example.crbtjetcompose.core.designsystem.R.string.core_designsystem_amount_placeholder),
            leadingIcon = {
                Text(text = stringResource(id = com.example.crbtjetcompose.core.data.R.string.core_data_ethio_currency))
            },
            colors = OutlinedTextFieldDefaults.colors(),
            onClear = {
                amountState.text = ""
            },
            showsErrors = amountState.showErrors(),
            errorText = amountState.getError() ?: "",
            keyboardActions = KeyboardActions(
                onDone = {
                    amountState.enableShowErrors()
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    amountState.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        amountState.enableShowErrors()
                    }
                },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonActionRow(
            onConfirmClick = {
                onConfirmTransfer(
                    userPhoneNumber.first,
                    amountState.text.toDoubleOrNull() ?: 0.0
                )
            },
            onDismissClick = onDismiss,
            actionText = stringResource(id = R.string.feature_services_transfer),
            actionLoading = actionLoading,
            actionEnabled = amountState.isValid && userPhoneNumber.second,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSheetContainer(
    title: String,
    content: @Composable () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                content()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}