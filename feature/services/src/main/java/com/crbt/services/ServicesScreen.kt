package com.crbt.services

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.CALL_ME_BACK_USSD
import com.crbt.data.core.data.util.CHECK_BALANCE_USSD
import com.crbt.data.core.data.util.TRANSFER_USSD
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.ui.core.ui.PermissionRequestComposable
import com.crbt.ui.core.ui.UssdResponseDialog
import com.example.crbtjetcompose.feature.services.R
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServicesRoute(
    navigateToPackages: () -> Unit,
    navigateToRecharge: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var crbtUssdType by remember {
        mutableStateOf(CrbtUssdType.BALANCE_CHECK)
    }
    val viewModel: ServicesViewModel = hiltViewModel()
    val ussdUiState by viewModel.ussdState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    PermissionRequestComposable(
        onPermissionsGranted = {
        }
    )

    ServicesScreen(
        onPackageClick = navigateToPackages,
        onRechargeClick = navigateToRecharge,
        onCheckBalance = {
            crbtUssdType = CrbtUssdType.BALANCE_CHECK
            viewModel.runUssdCode(
                ussdCode = CHECK_BALANCE_USSD,
                onSuccess = {
                    showDialog = true
                },
                onError = {
                    showDialog = true
                },
                ussdType = CrbtUssdType.BALANCE_CHECK,
                activity = context as Activity
            )
        },
        isCheckingBalance = ussdUiState is UssdUiState.Loading && crbtUssdType == CrbtUssdType.BALANCE_CHECK,
        onConfirmTransfer = { phoneNumber, amount ->
            crbtUssdType = CrbtUssdType.TRANSFER
            viewModel.runUssdCode(
                ussdCode = "$TRANSFER_USSD$amount*$phoneNumber#",
                onSuccess = {
                    showDialog = true
                },
                onError = {
                    showDialog = true
                },
                activity = context as Activity,
                ussdType = CrbtUssdType.TRANSFER
            )
        },
        onConfirmCallMeBack = { phoneNumber ->
            crbtUssdType = CrbtUssdType.CALL_ME_BACK
            viewModel.runUssdCode(
                ussdCode = "$CALL_ME_BACK_USSD$phoneNumber#",
                onSuccess = {
                    showDialog = true
                },
                onError = {
                    showDialog = true
                },
                ussdType = CrbtUssdType.CALL_ME_BACK,
                activity = context as Activity
            )
        },
        actionLoading = ussdUiState is UssdUiState.Loading,
    )
    if (showDialog) {
        UssdResponseDialog(
            onDismiss = {
                showDialog = false
            },
            ussdUiState = ussdUiState,
            crbtUssdType = crbtUssdType
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
    onConfirmCallMeBack: (String) -> Unit,
    onConfirmTransfer: (String, Double) -> Unit,
    actionLoading: Boolean,
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
                sheetState = sheetState,
                onDismiss = {
                    showBottomSheet = false
                    scope.launch {
                        sheetState.hide()
                    }
                },
                onConfirmCallMeBack = onConfirmCallMeBack,
                onConfirmTransfer = onConfirmTransfer,
                actionLoading = actionLoading,
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
            }
        }
    )
}
