package com.crbt.services

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.util.RECHARGE_USSD
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.UssdResponseDialog
import com.example.crbtjetcompose.feature.services.R
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RechargeScreen(
    rechargeViewModel: RechargeViewModel = hiltViewModel(),
    servicesViewModel: ServicesViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val ussdUiState by servicesViewModel.ussdState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val userPreferenceUiState by rechargeViewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val voucherCodeState = rechargeViewModel.voucherCodeState
    var showDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

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
            val pickPhoto = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    if (uri != null) {
                        rechargeViewModel.processImageUri(
                            uri, context,
                            onSuccess = { code ->
                                if (userData.autoDialRechargeCode) {
                                    servicesViewModel.runUssdCode(
                                        ussdCode = code.voucherToUssdCode(),
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
                            },
                            onError = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = it,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                },
            )
            val imageCapture = remember { ImageCapture.Builder().build() }
            RechargeContent(
                onCaptureClick = {
                    rechargeViewModel.captureImage(
                        context,
                        imageCapture,
                        onSuccess = { code ->
                            if (userData.autoDialRechargeCode) {
                                servicesViewModel.runUssdCode(
                                    ussdCode = code.voucherToUssdCode(),
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
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = it,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                },
                onDialClick = {
                    when (voucherCodeState) {
                        is VoucherCodeUiState.Success -> {
                            servicesViewModel.runUssdCode(
                                ussdCode = voucherCodeState.voucherCode.voucherToUssdCode(),
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

                        is VoucherCodeUiState.Error -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = voucherCodeState.message,
                                    duration = SnackbarDuration.Short
                                )
                            }
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
                voucherCodeUiState = voucherCodeState,
                imageCapture = imageCapture
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

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    imageCapture: ImageCapture
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    DisposableEffect(Unit) {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermissionGranted) {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
            return@DisposableEffect onDispose { }
        }

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier
    )
}


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
    voucherCodeUiState: VoucherCodeUiState,
    imageCapture: ImageCapture
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .clipToBounds()
        ) {
            CameraPreview(
                modifier = Modifier
                    .wrapContentHeight()
                    .clipToBounds(),
                imageCapture = imageCapture
            )
            if (voucherCodeUiState is VoucherCodeUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
            ScreenActionRow(
                onCaptureClick = onCaptureClick,
                onDialClick = onDialClick,
                onPickFromGalleryClick = onPickFromGalleryClick,
                showDialButton = showDialButton,
                isButtonEnabled = voucherCodeUiState !is VoucherCodeUiState.Loading
            )
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
    var showQuickSettings by remember { mutableStateOf(false) }
    var rechargeDigits by remember { mutableIntStateOf(numberOfRechargeCodeDigits) }
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
            .padding(bottom = 40.dp),
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
                value = rechargeDigits.toFloat(),
                onValueChange = { rechargeDigits = it.toInt() },
                onValueChangeFinished = { onRechargeCodeChanged(rechargeDigits) },
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


            SettingSwitch(
                title = stringResource(id = R.string.feature_services_recharge_screen_auto_dial_title),
                description = stringResource(id = R.string.feature_services_recharge_screen_auto_dial_description),
                isChecked = autoDialEnabled,
                onCheckedChange = onAutoDialEnabledChanged,
            )

            /* Row(
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
             }*/
        }
    }
}

@Composable
fun SettingSwitch(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            thumbContent = if (isChecked) {
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


@Composable
fun ScreenActionRow(
    onCaptureClick: () -> Unit,
    onDialClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    showDialButton: Boolean = true,
    isButtonEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(onClick = onCaptureClick, enabled = isButtonEnabled) {
            Icon(imageVector = CrbtIcons.Camera, contentDescription = null)
        }

        if (showDialButton) {
            FilledIconButton(onClick = onDialClick, enabled = isButtonEnabled) {
                Icon(imageVector = CrbtIcons.Phone, contentDescription = null)
            }
        }

        FilledIconButton(onClick = onPickFromGalleryClick, enabled = isButtonEnabled) {
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