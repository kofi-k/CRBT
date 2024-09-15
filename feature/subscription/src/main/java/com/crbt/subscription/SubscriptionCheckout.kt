package com.crbt.subscription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.DummyUser
import com.crbt.data.core.data.model.fullName
import com.crbt.data.core.data.util.simpleDateFormatPattern
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.InfoRow
import com.example.crbtjetcompose.feature.subscription.R
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SubscriptionCheckout(
    onDoneClicked: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SurfaceCard(content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = CrbtIcons.Info,
                    contentDescription = CrbtIcons.Info.name,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(52.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(MaterialTheme.typography.titleMedium.toSpanStyle()) {
                            append(stringResource(id = R.string.feature_subscription_etb))
                        }
                        withStyle(
                            MaterialTheme.typography.displayLarge.copy(
                                fontWeight = Bold
                            ).toSpanStyle()
                        ) {
                            append(DummyUser.user.accountBalance.toString()) // todo replace with top up amount
                        }
                    }
                )

                SurfaceCard(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = stronglyDeemphasizedAlpha
                    ),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            val dateFormat =
                                SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
                            val dateString = dateFormat.format(System.currentTimeMillis())
                            InfoRow(
                                title = stringResource(id = R.string.feature_subscription_from_text),
                                value = DummyUser.user.fullName(),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surface
                            )
                            InfoRow(
                                title = stringResource(id = R.string.feature_subscription_transaction_id),
                                value = "1234567890", // todo replace with transaction id
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surface
                            )
                            InfoRow(
                                title = stringResource(id = R.string.feature_subscription_date),
                                value = dateString,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                )
            }
        })

        OutlinedButton(
            onClick = { /*TODO share receipt logic here probably gonna be an intent or some shit like that*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = CrbtIcons.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.feature_subscription_share_receipt))
        }

        ProcessButton(
            text = stringResource(id = R.string.feature_subscription_done),
            onClick = onDoneClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }

}