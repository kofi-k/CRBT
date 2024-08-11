package com.crbt.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.example.crbtjetcompose.feature.services.R

@Composable
fun TopUpCheckoutScreen(
    onCompleteTopUp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SurfaceCard(
            content = {
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
                        text = stringResource(id = R.string.feature_services_amount_topup),
                        style = MaterialTheme.typography.headlineLarge,
                    )


                    Text(
                        text = buildAnnotatedString {
                            withStyle(MaterialTheme.typography.titleMedium.toSpanStyle()) {
                                append(stringResource(id = R.string.feature_services_etb, ""))
                            }
                            withStyle(
                                MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = Bold
                                ).toSpanStyle()
                            ) {
                                append("10") // todo replace with top up amount
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
                                InfoRow(
                                    title = stringResource(id = R.string.feature_services_account_balance),
                                    value = stringResource(
                                        id = R.string.feature_services_etb,
                                        DummyUser.user.accountBalance
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surface
                                )
                                InfoRow(
                                    title = stringResource(id = R.string.feature_services_topup_amount),
                                    value = stringResource(
                                        id = R.string.feature_services_etb,
                                        "10"
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surface
                                )
                                InfoRow(
                                    title = stringResource(id = R.string.feature_services_date),
                                    value = System.currentTimeMillis().toString(),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    )
                }
            }
        )

        ProcessButton(
            onClick = onCompleteTopUp,
            text = stringResource(id = R.string.feature_services_confirm),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun InfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                slightlyDeemphasizedAlpha
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                slightlyDeemphasizedAlpha
            )
        )
    }

}


@ThemePreviews
@Composable
fun TopUpCheckoutScreenPreview() {
    CrbtTheme {
        TopUpCheckoutScreen(
            onCompleteTopUp = {}
        )
    }
}