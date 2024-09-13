package com.crbt.services

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.CHECK_BALANCE_USSD
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.BalanceDialog
import com.example.crbtjetcompose.feature.services.R
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServicesRoute(
    navigateToPackages: () -> Unit,
    navigateToRecharge: () -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val viewModel: ServicesViewModel = hiltViewModel()
    val ussdUiState by viewModel.ussdState.collectAsStateWithLifecycle()
    ServicesScreen(
        onPackageClick = navigateToPackages,
        onRechargeClick = navigateToRecharge,
        onCheckBalance = {
            viewModel.runUssdCode(
                ussdCode = CHECK_BALANCE_USSD,
                onSuccess = {
                    showDialog = true
                },
                onError = {
                    showDialog = true
                }
            )
        },
        isCheckingBalance = ussdUiState is UssdUiState.Loading
    )
    if (showDialog) {
        BalanceDialog(
            onDismiss = {
                showDialog = false
            },
            ussdUiState = ussdUiState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    onPackageClick: () -> Unit,
    onRechargeClick: () -> Unit,
    onCheckBalance: () -> Unit,
    isCheckingBalance: Boolean,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    var bottomSheetType by remember { mutableStateOf(ServicesType.CALL_ME_BACK) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        UpperServices(
            onCheckBalance = onCheckBalance,
            onPackageClick = onPackageClick,
            onRechargeClick = onRechargeClick,
            isCheckingBalance = isCheckingBalance
        )
        LowerServices(
            onTransferClick = {
                showBottomSheet = true
                bottomSheetType = ServicesType.TRANSFER
            },
            onCallBackClick = {
                showBottomSheet = true
                bottomSheetType = ServicesType.CALL_ME_BACK
            },
            onTopUpClick = onRechargeClick //TODO change to onTopUpClick
        )
    }

    AnimatedVisibility(
        visible = showBottomSheet,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ServicesBottomSheet(
                servicesType = bottomSheetType,
                onPhoneNumberChanged = { _, _ -> },
                onAmountChange = { },
                sheetState = sheetState,
                onDismiss = {
                    showBottomSheet = false
                    scope.launch {
                        sheetState.hide()
                    }
                },
                onConfirmClick = { },
                actionLoading = false,
                actionEnabled = true
            )
        }
    }
}

@Composable
fun UpperServices(
    onCheckBalance: () -> Unit,
    onPackageClick: () -> Unit,
    onRechargeClick: () -> Unit,
    isCheckingBalance: Boolean,
) {
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                ListCard(
                    onClick = onCheckBalance,
                    headlineText = stringResource(id = R.string.feature_services_check_balance),
                    subText = stringResource(id = R.string.feature_services_check_description),
                    leadingContentIcon = CrbtIcons.Check,
                    trailingContent = {
                        if (isCheckingBalance) {
                            CircularProgressIndicator()
                        }
                    },
                    clickEnabled = !isCheckingBalance
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
    onTopUpClick: () -> Unit,
) {
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
                    onClick = onTopUpClick,
                    headlineText = stringResource(id = R.string.feature_services_topup),
                    subText = stringResource(id = R.string.feature_services_topup_amount),
                    leadingContentIcon = CrbtIcons.Dollar
                )
            }
        }
    )
}
