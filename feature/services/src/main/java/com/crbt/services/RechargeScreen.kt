package com.crbt.services

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.ui.core.ui.validationStates.AmountValidationState
import com.example.crbtjetcompose.feature.services.R


@Composable
fun RechargeScreen(
    onTopUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Balance(
            balance = DummyUser.user.accountBalance.toString(),
            modifier = Modifier.fillMaxWidth()
        )

        RechargeAmountCard(
            onAmountChange = {},
            modifier = Modifier.fillMaxWidth()
        )

        RechargeModeOfPaymentCard(
            onSelectedModeOfPayment = {},
            modifier = Modifier.fillMaxWidth()
        )

        ProcessButton(
            onClick = onTopUpClick,
            text = stringResource(id = R.string.feature_services_topup),
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun Balance(
    balance: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.feature_services_balance),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(id = R.string.feature_services_etb, balance),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black
            ),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}


@Composable
fun RechargeAmountCard(
    modifier: Modifier = Modifier,
    onAmountChange: (String) -> Unit,
) {
    val amountSatate by remember {
        mutableStateOf(AmountValidationState())
    }

    SurfaceCard(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                val focusManager = LocalFocusManager.current
                Text(
                    text = stringResource(id = R.string.feature_services_recharge_amount_title),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
                Text(
                    text = stringResource(id = R.string.feature_services_recharge_amount_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        slightlyDeemphasizedAlpha
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomInputField(
                    label = stringResource(id = R.string.feature_services_etb, ""),
                    value = amountSatate.text,
                    onValueChange = {
                        amountSatate.text = it
                        onAmountChange(amountSatate.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            amountSatate.onFocusChange(focusState.isFocused)
                            if (!focusState.isFocused) {
                                amountSatate.enableShowErrors()
                            }
                        },
                    inputType = InputType.MONEY,
                    onClear = {
                        amountSatate.text = ""
                        onAmountChange(amountSatate.text)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = CrbtIcons.Dollar,
                            contentDescription = CrbtIcons.Dollar.name,
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            amountSatate.enableShowErrors()
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
                    showsErrors = amountSatate.showErrors(),
                    errorText = amountSatate.getError() ?: "",
                )
            }
        }
    )
}


@Composable
fun RechargeModeOfPaymentCard(
    modifier: Modifier = Modifier,
    onSelectedModeOfPayment: (String) -> Unit,
) {
    SurfaceCard(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }

                Text(
                    text = stringResource(id = R.string.feature_services_mode_of_payment_title),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    ListCard(
                        onClick = {
                            expanded = !expanded
                        },
                        headlineText = stringResource(id = R.string.feature_services_payment_option),
                        leadingContentIcon = CrbtIcons.PaymentMethods,
                        trailingContent = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = CrbtIcons.ArrowForward,
                                    contentDescription = CrbtIcons.ArrowForward.name
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                    )
                    AnimatedVisibility(
                        visible = expanded,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // todo add payment options
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun RechargeAmountCardPreview() {
    RechargeAmountCard(onAmountChange = {})
}


@Preview(showBackground = true)
@Composable
fun RechargeScreenPreview() {
    RechargeScreen(
        onTopUpClick = {}
    )
}