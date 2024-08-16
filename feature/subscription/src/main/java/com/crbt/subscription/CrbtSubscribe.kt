package com.crbt.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.SubscriptionBillingType
import com.crbt.data.core.data.util.simpleDateFormatPattern
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.TextFieldType
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.CustomInputButton
import com.crbt.ui.core.ui.ShowDatePicker
import com.example.crbtjetcompose.feature.subscription.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@Composable
fun CrbtSubscribeScreen(
    onSubscriptionComplete: () -> Unit
) {

    var subscriptionName by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomInputField(
                    label = stringResource(id = R.string.feature_subscription_name_label),
                    value = subscriptionName,
                    onValueChange = {
                        subscriptionName = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    inputType = InputType.TEXT,
                    onClear = {
                        subscriptionName = ""
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = CrbtIcons.EditNote,
                            contentDescription = CrbtIcons.Album.name,
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        },
                    ),
                    colors = OutlinedTextFieldDefaults.colors(),
                    textFieldType = TextFieldType.OUTLINED
                )
                CustomInputField(
                    label = stringResource(id = R.string.feature_subscription_price_label),
                    value = "10.67", // todo replace with subscription price
                    onValueChange = {
                        subscriptionName = it
                    },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth(),
                    inputType = InputType.TEXT,
                    onClear = {
                        subscriptionName = ""
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = CrbtIcons.Dollar,
                            contentDescription = CrbtIcons.Dollar.name,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    textFieldType = TextFieldType.OUTLINED
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BillingType(
                    onBillingTypeSelected = {},
                    billingType = SubscriptionBillingType.Monthly
                )

                SubscriptionDate(
                    onDatePicked = {},
                    date = null
                )

                ProcessButton(
                    onClick = {
                        // do some viewmodel stuff for the subscription and then call the callback
                        onSubscriptionComplete()
                    },
                    text = stringResource(id = R.string.feature_subscription_button_text),
                )
            }
        }
    }
}


@Composable
fun BillingType(
    onBillingTypeSelected: (SubscriptionBillingType) -> Unit,
    billingType: SubscriptionBillingType
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(billingType) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(id = R.string.feature_subscription_billing_type_label))
        Spacer(modifier = Modifier.height(8.dp))
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            content = {
                Column {
                    CustomInputButton(
                        text = stringResource(id = selected.title),
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        leadingIcon = CrbtIcons.PaymentMethods
                    )
                    if (expanded) {
                        SubscriptionBillingType.entries.forEach {
                            ListItem(
                                headlineContent = { Text(text = stringResource(id = it.title)) },
                                modifier = Modifier.clickable {
                                    selected = it
                                    expanded = false
                                    onBillingTypeSelected(it)
                                },
                            )
                        }
                    }
                }
            },
            color = MaterialTheme.colorScheme.outlineVariant.copy(
                stronglyDeemphasizedAlpha,
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDate(
    onDatePicked: (Long) -> Unit,
    date: Long?,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
    var pickedDate by remember { mutableStateOf(date) }

    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dateString = pickedDate?.let { dateFormat.format(it) }
        ?: stringResource(id = R.string.feature_subscription_date_placeholder)

    CustomInputButton(
        text = dateString,
        leadingIcon = CrbtIcons.Calendar,
        onClick = { showDatePicker = true },
        trailingIcon = {
            Icon(
                imageVector = CrbtIcons.ArrowRight,
                contentDescription = CrbtIcons.ArrowRight.name,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    )

    AnimatedVisibility(
        visible = showDatePicker,
        enter = expandVertically(),
        exit = shrinkOut()
    ) {
        ShowDatePicker(
            title = stringResource(id = R.string.feature_subscription_date_title),
            onDateSelected = {
                onDatePicked(it)
                pickedDate = it
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@ThemePreviews
@Composable
fun CrbtSubscribeScreenPreview() {
    CrbtTheme {
        CrbtSubscribeScreen {}
    }
}