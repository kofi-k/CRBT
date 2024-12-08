package com.crbt.services

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.util.RECHARGE_USSD
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.PermissionRequestComposable
import com.crbt.ui.core.ui.UssdResponseDialog
import com.example.crbtjetcompose.feature.services.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RechargeScreen(
    rechargeViewModel: RechargeViewModel = hiltViewModel(),
    servicesViewModel: ServicesViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val ussdUiState by servicesViewModel.ussdState.collectAsStateWithLifecycle()

    val userPreferenceUiState by rechargeViewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val voucherCodeState by rechargeViewModel.voucherCodeState.collectAsStateWithLifecycle()

    var showDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                rechargeViewModel.processImageUri(uri, context)
            }
        },
    )

    PermissionRequestComposable(
        onPermissionsGranted = {
        }
    )

    LaunchedEffect(voucherCodeState) {
        when (val state = voucherCodeState) {
            is VoucherCodeUiState.Success -> {
                val userData = (userPreferenceUiState as UserPreferenceUiState.Success).userData
                if (userData.autoDialRechargeCode) {
                    servicesViewModel.runUssdCode(
                        ussdCode = state.voucherCode.voucherToUssdCode(),
                        onSuccess = {
                            showDialog = true
                        },
                        onError = {
                            showDialog = true
                        },
                        activity = context as Activity,
                        ussdType = CrbtUssdType.RECHARGE
                    )
                }
            }

            else -> {}
        }

    }

    when (userPreferenceUiState) {
        UserPreferenceUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                )
            }
        }

        is UserPreferenceUiState.Success -> {
            val userData = (userPreferenceUiState as UserPreferenceUiState.Success).userData
            RechargeContent(
                onCaptureClick = {},
                onDialClick = {
                    when (val state = voucherCodeState) {
                        is VoucherCodeUiState.Success -> {
                            servicesViewModel.runUssdCode(
                                ussdCode = state.voucherCode.voucherToUssdCode(),
                                onSuccess = {
                                    showDialog = true
                                },
                                onError = {
                                    showDialog = true
                                },
                                activity = context as Activity,
                                ussdType = CrbtUssdType.RECHARGE
                            )
                        }

                        else -> {}
                    }
                },
                onPickFromGalleryClick = {
                    pickPhoto.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
                numberOfRechargeCodeDigits = userData.numberOfRechargeCodeDigits,
                onRechargeCodeChanged = rechargeViewModel::onRechargeCodeChanged,
                autoDialEnabled = userData.autoDialRechargeCode,
                onAutoDialEnabledChanged = rechargeViewModel::onAutoDialEnabledChanged,
                showDialButton = !userData.autoDialRechargeCode,
                voucherCodeUiState = voucherCodeState
            )
        }
    }

    if (showDialog) {
        UssdResponseDialog(
            onDismiss = {
                showDialog = false
                navigateUp()
            },
            ussdUiState = ussdUiState,
            crbtUssdType = CrbtUssdType.RECHARGE
        )
    }

}

/*@Composable
fun CameraPreview(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) { view ->
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(view.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}*/


@Composable
fun RechargeContent(
    onCaptureClick: () -> Unit,
    onDialClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    numberOfRechargeCodeDigits: Int,
    onRechargeCodeChanged: (Int) -> Unit,
    autoDialEnabled: Boolean,
    onAutoDialEnabledChanged: (Boolean) -> Unit,
    showDialButton: Boolean,
    voucherCodeUiState: VoucherCodeUiState
) {
    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
//                    CameraPreview(
//                        modifier = Modifier.fillMaxSize()
//                    )
                    if (voucherCodeUiState is VoucherCodeUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))



                Text(
                    text =
                    when (voucherCodeUiState) {
                        is VoucherCodeUiState.Success -> {
                            voucherCodeUiState.voucherCode
                        }

                        is VoucherCodeUiState.Error -> {
                            voucherCodeUiState.message
                        }

                        else -> {
                            stringResource(id = R.string.feature_services_recharge_screen_title)
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            ScreenActionRow(
                onCaptureClick = onCaptureClick,
                onDialClick = onDialClick,
                onPickFromGalleryClick = onPickFromGalleryClick,
                showDialButton = showDialButton,
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuickSettings(
                numberOfRechargeCodeDigits = numberOfRechargeCodeDigits,
                onRechargeCodeChanged = { onRechargeCodeChanged(it) },
                autoDialEnabled = autoDialEnabled,
                onAutoDialEnabledChanged = onAutoDialEnabledChanged,
            )
        }
    }
}


@Composable
fun QuickSettings(
    numberOfRechargeCodeDigits: Int,
    onRechargeCodeChanged: (Int) -> Unit,
    autoDialEnabled: Boolean,
    onAutoDialEnabledChanged: (Boolean) -> Unit,
) {
    var showQuickSettings by remember { mutableStateOf(true) }
    val rotateDegrees by animateFloatAsState(
        targetValue = if (showQuickSettings) {
            0f
        } else {
            180f
        },
        label = "rotateDegrees"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showQuickSettings = !showQuickSettings }
        ) {
            Icon(
                imageVector = CrbtIcons.Settings,
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(rotateDegrees),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when {
                    showQuickSettings -> stringResource(id = R.string.feature_services_recharge_screen_quick_settings_hide)
                    else -> stringResource(id = R.string.feature_services_recharge_screen_quick_settings_show)
                },
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showQuickSettings) {
            Slider(
                value = numberOfRechargeCodeDigits.toFloat(),
                onValueChange = { onRechargeCodeChanged(it.toInt()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 20,
                valueRange = 10f..30f
            )
            Text(
                text = numberOfRechargeCodeDigits.toString(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.feature_services_recharge_screen_recharge_code_length),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.feature_services_recharge_screen_auto_dial_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(id = R.string.feature_services_recharge_screen_auto_dial_description),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
                    )
                }

                Switch(
                    checked = autoDialEnabled,
                    onCheckedChange = onAutoDialEnabledChanged,
                    thumbContent = if (autoDialEnabled) {
                        {
                            Icon(
                                imageVector = CrbtIcons.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}


@Composable
fun ScreenActionRow(
    onCaptureClick: () -> Unit,
    onDialClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    showDialButton: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(onClick = onCaptureClick) {
            Icon(imageVector = CrbtIcons.Camera, contentDescription = null)
        }

        if (showDialButton) {
            FilledIconButton(onClick = onDialClick) {
                Icon(imageVector = CrbtIcons.Phone, contentDescription = null)
            }
        }

        FilledIconButton(onClick = onPickFromGalleryClick) {
            Icon(imageVector = CrbtIcons.Photos, contentDescription = null)
        }

    }
}

@Preview
@Composable
fun ScreenActionRowPreview() {
    ScreenActionRow(
        onCaptureClick = {},
        onDialClick = {},
        onPickFromGalleryClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun QuickSettingsPreview() {
    QuickSettings(
        numberOfRechargeCodeDigits = 20,
        onRechargeCodeChanged = {},
        autoDialEnabled = true,
        onAutoDialEnabledChanged = {},
    )
}

fun String.voucherToUssdCode(): String {
    return "$RECHARGE_USSD$this#"
}