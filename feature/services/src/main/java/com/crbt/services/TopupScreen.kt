package com.crbt.services

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.CrbtPaymentOptions
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.TextFieldType
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.validationStates.AmountValidationState
import com.itengs.crbt.feature.services.R


@Composable
fun TopupScreen(
    onTopUpClick: (String) -> Unit,
    viewModel: ServicesViewModel = hiltViewModel(),
    topUpViewModel: TopUpViewModel = hiltViewModel()
) {
    val userPreferenceUiState by viewModel.userPreferenceUiState.collectAsStateWithLifecycle()

    var isAmountValid by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedModeOfPayment by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Balance(
            balance = when (userPreferenceUiState) {
                is UserPreferenceUiState.Success -> {
                    (userPreferenceUiState as UserPreferenceUiState.Success).userData.currentBalance.toString()
                }

                else -> DummyUser.user.accountBalance.toString()
            },
            modifier = Modifier.fillMaxWidth()
        )

        RechargeAmountCard(
            onAmountChange = { amount, isValid ->
                topUpViewModel.onAmountChange(amount)
                isAmountValid = isValid
            },
        )

        RechargeModeOfPaymentCard(
            onSelectedModeOfPayment = {
                selectedModeOfPayment = it
            },
            listOfPaymentOptions = CrbtPaymentOptions.entries,
        )

        ProcessButton(
            onClick = { onTopUpClick(topUpViewModel.amount) },
            text = stringResource(id = R.string.feature_services_topup),
            modifier = Modifier.fillMaxWidth(),
            isEnabled = isAmountValid && selectedModeOfPayment != 0
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
    onAmountChange: (String, Boolean) -> Unit,
) {
    val amountState by remember {
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
                    value = amountState.text,
                    onValueChange = {
                        amountState.text = it
                        onAmountChange(amountState.text, amountState.isValid)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            amountState.onFocusChange(focusState.isFocused)
                            if (!focusState.isFocused) {
                                amountState.enableShowErrors()
                            }
                        },
                    inputType = InputType.MONEY,
                    onClear = {
                        amountState.text = ""
                        onAmountChange(amountState.text, amountState.isValid)
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
                            amountState.enableShowErrors()
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
                    colors = OutlinedTextFieldDefaults.colors(),
                    textFieldType = TextFieldType.OUTLINED,
                    showsErrors = amountState.showErrors(),
                    errorText = amountState.getError() ?: "",
                )
            }
        }
    )
}


@Composable
fun RechargeModeOfPaymentCard(
    onSelectedModeOfPayment: (Int) -> Unit,
    listOfPaymentOptions: List<CrbtPaymentOptions>
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
                var selectedModeOfPayment by remember {
                    mutableIntStateOf(0)
                }

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
                    val rotateIcon by animateFloatAsState(
                        targetValue = if (expanded) 90f else 0f,
                        label = "rotateIcon"
                    )

                    ListCard(
                        onClick = {
                            expanded = !expanded
                        },
                        headlineText = stringResource(
                            id = CrbtPaymentOptions.entries.find { it.id == selectedModeOfPayment }?.title
                                ?: R.string.feature_services_payment_option
                        ),
                        leadingContentIcon = CrbtIcons.PaymentMethods,
                        trailingContent = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = CrbtIcons.ArrowRight,
                                    contentDescription = CrbtIcons.ArrowForward.name,
                                    modifier = Modifier.rotate(rotateIcon)
                                )
                            }
                        },
                        leadingContent = {
                            val selectedOption =
                                CrbtPaymentOptions.entries.find { it.id == selectedModeOfPayment }
                            when (selectedOption) {
                                null -> Icon(
                                    imageVector = CrbtIcons.PaymentMethods,
                                    contentDescription = CrbtIcons.PaymentMethods.name,
                                )

                                else -> Image(
                                    painter = painterResource(id = selectedOption.imageRes),
                                    contentDescription = stringResource(id = selectedOption.title),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                    )
                    if (expanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            listOfPaymentOptions.forEachIndexed { _, crbtPaymentOptions ->
                                ListCard(
                                    onClick = {
                                        selectedModeOfPayment = crbtPaymentOptions.id
                                        onSelectedModeOfPayment(crbtPaymentOptions.id)
                                        expanded = false
                                    },
                                    headlineText = stringResource(id = crbtPaymentOptions.title),
                                    leadingContentIcon = CrbtIcons.PaymentMethods,
                                    leadingContent = {
                                        Image(
                                            painter = painterResource(id = crbtPaymentOptions.imageRes),
                                            contentDescription = stringResource(id = crbtPaymentOptions.title),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(50.dp)
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
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
    RechargeAmountCard(onAmountChange = { _, _ -> })
}


@Preview(showBackground = true)
@Composable
fun RechargeScreenPreview() {
    TopupScreen(
        onTopUpClick = {}
    )
}