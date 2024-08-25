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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.TextFieldType
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.services.packages.ButtonActionRow
import com.crbt.services.packages.GiftPurchaseContent
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
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    onAmountChange: (String) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    actionLoading: Boolean,
    actionEnabled: Boolean
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
                onPhoneNumberChanged = onPhoneNumberChanged,
                onAmountChange = onAmountChange,
                onConfirmCallMeBack = onConfirmClick,
                onConfirmTransfer = onConfirmClick,
                actionLoading = actionLoading,
                actionEnabled = actionEnabled,
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
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    onAmountChange: (String) -> Unit,
    onConfirmCallMeBack: () -> Unit,
    onConfirmTransfer: () -> Unit,
    onDismiss: () -> Unit,
    actionLoading: Boolean,
    actionEnabled: Boolean
) {
    val actionText = when (servicesType) {
        ServicesType.CALL_ME_BACK ->
            stringResource(id = R.string.feature_services_call_back_button)

        ServicesType.TRANSFER ->
            stringResource(id = R.string.feature_services_transfer)
    }
    when (servicesType) {
        ServicesType.CALL_ME_BACK -> {
            CallMeBackContent(
                onPhoneNumberChanged = onPhoneNumberChanged,
                modifier = Modifier,
                onConfirmCallMeBack = onConfirmCallMeBack,
                onDismiss = onDismiss,
                actionText = actionText,
                actionLoading = actionLoading,
                actionEnabled = actionEnabled
            )
        }

        ServicesType.TRANSFER -> {
            TransferContent(
                onPhoneNumberChanged = onPhoneNumberChanged,
                onAmountChange = onAmountChange,
                modifier = Modifier,
                onConfirmTransfer = onConfirmTransfer,
                onDismiss = onDismiss,
                actionText = actionText,
                actionLoading = actionLoading,
                actionEnabled = actionEnabled
            )
        }
    }
}


@Composable
fun CallMeBackContent(
    modifier: Modifier = Modifier,
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    onConfirmCallMeBack: () -> Unit,
    onDismiss: () -> Unit,
    actionText: String,
    actionLoading: Boolean,
    actionEnabled: Boolean
) {
    Column(
        modifier = modifier
    ) {
        GiftPurchaseContent(
            onPhoneNumberChanged = onPhoneNumberChanged,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonActionRow(
            onConfirmClick = onConfirmCallMeBack,
            onDismissClick = onDismiss,
            actionText = actionText,
            actionLoading = actionLoading,
            actionEnabled = actionEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TransferContent(
    onPhoneNumberChanged: (String, Boolean) -> Unit,
    onAmountChange: (String) -> Unit,
    modifier: Modifier,
    onConfirmTransfer: () -> Unit,
    onDismiss: () -> Unit,
    actionText: String,
    actionLoading: Boolean,
    actionEnabled: Boolean
) {
    val focusManager = LocalFocusManager.current
    val amountState by remember {
        mutableStateOf(AmountValidationState())
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        GiftPurchaseContent(
            onPhoneNumberChanged = onPhoneNumberChanged,
            modifier = Modifier.fillMaxWidth()
        )
        CustomInputField(
            onValueChange = {
                amountState.text = it
                onAmountChange(it)
            },
            value = amountState.text,
            inputType = InputType.MONEY,
            textFieldType = TextFieldType.OUTLINED,
            label = stringResource(id = com.example.crbtjetcompose.core.designsystem.R.string.core_designsystem_amount_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = CrbtIcons.Money,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = OutlinedTextFieldDefaults.colors(),
            onClear = {
                amountState.text = ""
                onAmountChange("")
            },
            showsErrors = amountState.showErrors(),
            errorText = amountState.getError() ?: "",
            keyboardActions = KeyboardActions(
                onDone = {
                    amountState.enableShowErrors()
                    onAmountChange(amountState.text)
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonActionRow(
            onConfirmClick = onConfirmTransfer,
            onDismissClick = onDismiss,
            actionText = actionText,
            actionLoading = actionLoading,
            actionEnabled = actionEnabled,
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