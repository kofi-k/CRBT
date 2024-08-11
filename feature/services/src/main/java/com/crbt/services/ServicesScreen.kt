package com.crbt.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.feature.services.R

@Composable
fun ServicesScreen(
    onCheckClick: () -> Unit,
    onPackageClick: () -> Unit,
    onRechargeClick: () -> Unit,
    onTransferClick: () -> Unit,
    onCallBackClick: () -> Unit,
    onBuyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        UpperServices(
            onCheckClick = onCheckClick,
            onPackageClick = onPackageClick,
            onRechargeClick = onRechargeClick
        )
        LowerServices(
            onTransferClick = onTransferClick,
            onCallBackClick = onCallBackClick,
            onBuyClick = onBuyClick
        )

    }
}

@Composable
fun UpperServices(
    onCheckClick: () -> Unit,
    onPackageClick: () -> Unit,
    onRechargeClick: () -> Unit,
) {
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                ListCard(
                    onClick = onCheckClick,
                    headlineText = stringResource(id = R.string.feature_services_check_balance),
                    subText = stringResource(id = R.string.feature_services_check_description),
                    leadingContentIcon = CrbtIcons.Check
                )
                ListCard(
                    onClick = onPackageClick,
                    headlineText = stringResource(id = R.string.feature_services_packages),
                    subText = stringResource(id = R.string.feature_services_packages_description),
                    leadingContentIcon = CrbtIcons.Packages
                )
                ListCard(
                    onClick = onRechargeClick,
                    headlineText = stringResource(id = R.string.feature_services_recharge),
                    subText = stringResource(id = R.string.feature_services_recharge_description),
                    leadingContentIcon = CrbtIcons.Recharge
                )
            }
        }
    )

}

@Composable
fun LowerServices(
    onTransferClick: () -> Unit,
    onCallBackClick: () -> Unit,
    onBuyClick: () -> Unit,
){
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                ListCard(
                    onClick = onTransferClick,
                    headlineText = stringResource(id = R.string.feature_services_transfer),
                    subText = stringResource(id = R.string.feature_services_transfer_description),
                    leadingContentIcon = CrbtIcons.Services
                )
                ListCard(
                    onClick = onCallBackClick,
                    headlineText = stringResource(id = R.string.feature_services_call_back),
                    subText = stringResource(id = R.string.feature_services_call_back_description),
                    leadingContentIcon = CrbtIcons.CallBack
                )
                ListCard(
                    onClick = onBuyClick,
                    headlineText = stringResource(id = R.string.feature_services_buy),
                    subText = stringResource(id = R.string.feature_services_buy_description),
                    leadingContentIcon = CrbtIcons.Dollar
                )
            }
        }
    )
}