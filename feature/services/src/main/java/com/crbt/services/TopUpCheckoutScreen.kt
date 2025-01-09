package com.crbt.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.model.DummyUser
import com.crbt.data.core.data.util.simpleDateFormatPattern
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.InfoRow
import com.itengs.crbt.feature.services.R
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TopUpCheckoutScreen(
    onCompleteTopUp: () -> Unit,
) {
    val viewModel: ServicesViewModel = hiltViewModel()
    val userPreferenceUiState by viewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val topUpAmount by viewModel.topUpAmount.collectAsStateWithLifecycle()
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
                                append(topUpAmount)
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
                                    title = stringResource(id = R.string.feature_services_account_balance),
                                    value = stringResource(
                                        id = R.string.feature_services_etb,
                                        when (userPreferenceUiState) {
                                            is UserPreferenceUiState.Success -> {
                                                (userPreferenceUiState as UserPreferenceUiState.Success).userData.currentBalance
                                            }

                                            else -> DummyUser.user.accountBalance
                                        }
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
                                        topUpAmount ?: ""
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surface
                                )
                                InfoRow(
                                    title = stringResource(id = R.string.feature_services_date),
                                    value = dateString,
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


@ThemePreviews
@Composable
fun TopUpCheckoutScreenPreview() {
    CrbtTheme {
        TopUpCheckoutScreen(
            onCompleteTopUp = {},
        )
    }
}