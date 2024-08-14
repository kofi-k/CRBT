package com.crbt.onboarding

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.otp.OtpScreen
import com.example.crbtjetcompose.feature.onboarding.R

@RequiresApi(VERSION_CODES.TIRAMISU)
@Composable
fun OTPVerification(
    modifier: Modifier = Modifier,
) {
    OnboardingSheetContainer(
        modifier = modifier,
        titleRes = R.string.feature_onboarding_authentication_title,
        subtitleRes = R.string.feature_onboarding_authentication_subtitle,
        content = {
            OTPVerificationScreen(
                onOtpModified = { otp, isComplete ->
                    // todo
                }
            )
        }
    )
}

@RequiresApi(VERSION_CODES.TIRAMISU)
@Composable
fun OTPVerificationScreen(
    onOtpModified: (String, Boolean) -> Unit = { _, _ -> },
) {
    OtpScreen(
        otpValue = "1234",
        onOtpModified = { otp, isComplete ->
            onOtpModified(otp, isComplete)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp,
            ),
    )
}

@Preview(showBackground = true)
@RequiresApi(VERSION_CODES.TIRAMISU)
@Composable
fun PreviewOTPVerification() {
    CrbtTheme {
        OTPVerification(
            modifier = Modifier.fillMaxWidth()
        )
    }
}