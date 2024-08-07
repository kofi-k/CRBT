package com.crbt.onboarding

import android.os.Build
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.data.core.data.CRBTLanguage
import com.crbt.data.core.data.OnboardingScreenData
import com.crbt.data.core.data.OnboardingSetupData
import com.crbt.data.core.data.OnboardingSetupProcess
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.feature.onboarding.R
import kotlinx.coroutines.launch
import com.example.crbtjetcompose.core.ui.R as UiR

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOTPVerified: () -> Unit,
    screenData: OnboardingScreenData,
    onboardingSetupData: OnboardingSetupData,
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean,
    onLanguageSelected: (CRBTLanguage) -> Unit,
    onPhoneNumberEntered: (String, Boolean) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
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

        Column(
            modifier = Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier =
                Modifier
                    .fillMaxHeight(0.5f)
            ) {
                Image(
                    painter = painterResource(id = UiR.drawable.ui_crbtlogo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(id = R.string.feature_onboarding_page_one_title),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                )

                Text(
                    text = stringResource(id = R.string.feature_onboarding_page_one_subtitle),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                IconButton(
                    onClick = {
                        showBottomSheet = true
                        scope.launch { sheetState.show() }
                    },
                ) {
                    Icon(
                        imageVector = CrbtIcons.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier
                            .size(72.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                scope.launch { sheetState.hide() }
            },
            sheetState = sheetState,
            windowInsets = WindowInsets(0, 0, 0, 0),
            scrimColor = Color.Black.copy(alpha = 0.5f),
        ) {
            CrbtOnboardingBottomSheet(
                onOTPVerified = onOTPVerified,
                screenData = screenData,
                onboardingSetupData = onboardingSetupData,
                onNextClicked = onNextClicked,
                isNextEnabled = isNextEnabled,
                onLanguageSelected = onLanguageSelected,
                onPhoneNumberEntered = onPhoneNumberEntered
            )
        }
    }
}

private const val CONTENT_ANIMATION_DURATION = 300

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
internal fun CrbtOnboardingBottomSheet(
    onOTPVerified: () -> Unit,
    screenData: OnboardingScreenData,
    onboardingSetupData: OnboardingSetupData,
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean,
    onLanguageSelected: (CRBTLanguage) -> Unit,
    onPhoneNumberEntered: (String, Boolean) -> Unit,

    ) {


    BottomSheetContent(
        onNextClicked = onNextClicked,
        isNextButtonEnabled = isNextEnabled,
        onShowVerifyOtp = screenData.onboardingSetupProcess == OnboardingSetupProcess.OTP_VERIFICATION,
        onOTPVerified = onOTPVerified,
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
                            onPhoneNumberChanged = onPhoneNumberEntered
                        )
                    }

                    OnboardingSetupProcess.OTP_VERIFICATION -> {
                        OTPVerification(
                            modifier = Modifier.fillMaxWidth(),
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
    onNextClicked: () -> Unit,
    onOTPVerified: () -> Unit,
    isNextButtonEnabled: Boolean,
    onShowVerifyOtp: Boolean,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        content()
        Spacer(modifier = Modifier.height(32.dp))
        ProcessButton(
            onClick = if (onShowVerifyOtp) onOTPVerified else onNextClicked,
            text = stringResource(
                id =  if (onShowVerifyOtp) R.string.feature_onboarding_verify_button else
                R.string.feature_onboarding_next_button
            )
            ,
            isEnabled = if (onShowVerifyOtp) true else isNextButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    val viewModel: OnboardingViewModel = hiltViewModel()
    OnboardingScreen(
        onOTPVerified = {},
        screenData = viewModel.onboardingScreenData,
        onboardingSetupData = viewModel.onboardingSetupData,
        onNextClicked = {},
        isNextEnabled = true,
        onLanguageSelected = {},
        onPhoneNumberEntered = { _, _ -> }
    )
}