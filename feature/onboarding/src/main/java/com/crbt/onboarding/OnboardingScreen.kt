package com.crbt.onboarding

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.data.core.data.OnboardingSetupProcess
import com.crbt.data.core.data.model.OnboardingScreenData
import com.crbt.data.core.data.model.OnboardingSetupData
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.onboarding.ui.LanguageSelection
import com.crbt.onboarding.ui.OTPVerification
import com.crbt.onboarding.ui.OnboardingViewModel
import com.crbt.onboarding.ui.PhoneNumberInput
import com.crbt.onboarding.ui.phoneAuth.AuthState
import com.crbt.onboarding.ui.phoneAuth.PhoneAuthViewModel
import com.crbt.ui.core.ui.PermissionRequestComposable
import com.example.crbtjetcompose.feature.onboarding.R
import kotlinx.coroutines.launch
import com.example.crbtjetcompose.core.ui.R as UiR

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navigateToHome: () -> Unit,
    navigateToProfileSetup: () -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: OnboardingViewModel = hiltViewModel()
    val phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel()
    val authState = phoneAuthViewModel.authState
    val screenData = viewModel.onboardingScreenData
    val onboardingSetupData = viewModel.onboardingSetupData
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        viewModel.onPreviousClicked()
    }

    PermissionRequestComposable(
        onPermissionsGranted = {}
    )


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CrbtOnboardingBottomSheet(
                screenData = screenData,
                onboardingSetupData = onboardingSetupData,
                onNextClicked = {
                    when (screenData.onboardingSetupProcess) {

                        OnboardingSetupProcess.PHONE_NUMBER_ENTRY -> {
                            phoneAuthViewModel.sendVerificationCode(
                                onOtpSent = { message ->
                                    viewModel.onNextClicked()
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = message,
                                            duration = SnackbarDuration.Short,
                                        )
                                    }
                                },
                                onFailed = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = message,
                                            duration = SnackbarDuration.Short,
                                        )
                                    }
                                },
                                phoneNumber = onboardingSetupData.phoneNumber,
                                activity = context as Activity
                            )
                        }

                        OnboardingSetupProcess.OTP_VERIFICATION -> {
                            phoneAuthViewModel.verifyCode(
                                otpCode = viewModel.otpCode,
                                phone = onboardingSetupData.phoneNumber,
                                accountType = "user",
                                langPref = onboardingSetupData.selectedLanguage,
                                onFailed = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = message,
                                            duration = SnackbarDuration.Short,
                                        )
                                    }
                                },
                                onVerified = { userName ->
                                    if (userName.isNotBlank()) {
                                        navigateToHome()
                                    } else {
                                        navigateToProfileSetup()
                                    }
                                    viewModel.onDoneClicked()
                                    scope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.hide()
                                    }
                                }
                            )
                        }

                        else -> {
                            viewModel.onNextClicked()
                        }
                    }
                },
                isNextEnabled = viewModel.isNextEnabled,
                onPhoneNumberEntered = viewModel::onPhoneNumberEntered,
                onLanguageSelected = { code ->
                    viewModel.onLanguageSelected(code)
//                    AppCompatDelegate.setApplicationLocales(
//                        LocaleListCompat.forLanguageTags(code)
//                    )
                },
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Vertical,
                        ),
                    ),
                buttonLoading = authState is AuthState.Loading,
                onOtpModified = viewModel::onOtpCodeChanged,
                otpValue = viewModel.otpCode,
                phoneFieldEnabled = authState !is AuthState.Loading
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets(0, 0, 0, 0)
            ),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        val scrim = if (bottomSheetScaffoldState.bottomSheetState.isVisible) {
            Color.Black.copy(alpha = 0.5f)
        } else {
            Color.Transparent
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrim),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.onboarding_onboardingbackground),
                contentDescription = "background image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )
        }
        OnboardingTextContent(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            scope.launch {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }
}

@Composable
fun OnboardingTextContent(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight(0.5f)
        ) {
            Image(
                painter = painterResource(id = UiR.drawable.core_ui_crbtlogo),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight(0.5f)
        ) {
            Text(
                text = stringResource(id = R.string.feature_onboarding_page_one_title),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.feature_onboarding_page_one_subtitle),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            IconButton(
                onClick = onClicked,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = CrbtIcons.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(86.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

}

private const val CONTENT_ANIMATION_DURATION = 300

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
internal fun CrbtOnboardingBottomSheet(
    screenData: OnboardingScreenData,
    onboardingSetupData: OnboardingSetupData,
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean,
    onLanguageSelected: (String) -> Unit,
    onPhoneNumberEntered: (String, Boolean) -> Unit,
    onOtpModified: (String, Boolean) -> Unit,
    otpValue: String,
    modifier: Modifier = Modifier,
    buttonLoading: Boolean,
    phoneFieldEnabled: Boolean,
) {
    val buttonText = when (screenData.onboardingSetupProcess) {
        OnboardingSetupProcess.OTP_VERIFICATION -> {
            stringResource(id = R.string.feature_onboarding_verify_button)
        }

        else -> {
            stringResource(id = R.string.feature_onboarding_next_button)
        }
    }
    BottomSheetContent(
        modifier = modifier,
        onNextClicked = onNextClicked,
        isNextButtonEnabled = isNextEnabled,
        buttonText = buttonText,
        buttonLoading = buttonLoading,
        content = {
            AnimatedContent(
                targetState = screenData,
                label = "onboardingScreenDataAnimation",
                transitionSpec = {
                    val animationSpec: TweenSpec<IntOffset> =
                        tween(CONTENT_ANIMATION_DURATION)
                    val direction = getTransitionDirection(
                        initialIndex = initialState.onboardingScreenIndex,
                        targetIndex = targetState.onboardingScreenIndex,
                    )
                    slideIntoContainer(
                        towards = direction,
                        animationSpec = animationSpec,
                    ) togetherWith slideOutOfContainer(
                        towards = direction,
                        animationSpec = animationSpec,
                    )
                },
            ) { targetState ->
                when (targetState.onboardingSetupProcess) {
                    OnboardingSetupProcess.LANGUAGE_SELECTION -> {
                        LanguageSelection(
                            onLanguageSelected = onLanguageSelected,
                            selectedLanguage = onboardingSetupData.selectedLanguage
                        )
                    }

                    OnboardingSetupProcess.PHONE_NUMBER_ENTRY -> {
                        PhoneNumberInput(
                            onPhoneNumberChanged = onPhoneNumberEntered,
                            enabled = phoneFieldEnabled
                        )
                    }

                    OnboardingSetupProcess.OTP_VERIFICATION -> {
                        OTPVerification(
                            modifier = Modifier.fillMaxWidth(),
                            onOtpModified = onOtpModified,
                            otpValue = otpValue,
                            phoneNumber = onboardingSetupData.phoneNumber
                        )
                    }

                    else -> {}
                }
            }
        }
    )

}

private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int,
): AnimatedContentTransitionScope.SlideDirection {
    return if (targetIndex > initialIndex) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}


@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    onNextClicked: () -> Unit,
    isNextButtonEnabled: Boolean,
    content: @Composable () -> Unit,
    buttonLoading: Boolean,
    buttonText: String = stringResource(id = R.string.feature_onboarding_next_button),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier),
    ) {
        content()
        Spacer(modifier = Modifier.height(32.dp))
        ProcessButton(
            onClick = onNextClicked,
            text = buttonText,
            isEnabled = isNextButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
            isProcessing = buttonLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun OnboardingScreenPreview() {
    CrbtTheme {
        OnboardingTextContent(
            onClicked = {},
        )
    }
}