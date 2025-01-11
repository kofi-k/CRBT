package com.crbt.onboarding.ui

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.otp.OtpScreen
import com.itengs.crbt.feature.onboarding.R


@RequiresApi(VERSION_CODES.TIRAMISU)
@Composable
fun OTPVerification(
    modifier: Modifier = Modifier,
    onOtpModified: (String, Boolean) -> Unit,
    otpValue: String,
    phoneNumber: String,
) {
    OnboardingSheetContainer(
        modifier = modifier,
        title = stringResource(id = R.string.feature_onboarding_authentication_title),
        subtitle = stringResource(
            id = R.string.feature_onboarding_authentication_subtitle,
            phoneNumber
        ),
        content = {
            OTPVerificationScreen(
                onOtpModified = onOtpModified,
                otpValue = otpValue,
            )
        }
    )
}

@RequiresApi(VERSION_CODES.TIRAMISU)
@Composable
fun OTPVerificationScreen(
    onOtpModified: (String, Boolean) -> Unit = { _, _ -> },
    otpValue: String,
) {
    OtpScreen(
        otpValue = otpValue,
        onOtpModified = onOtpModified,
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
            modifier = Modifier.fillMaxWidth(),
            onOtpModified = { _, _ -> },
            otpValue = "123456",
            phoneNumber = "123456789"
        )
    }
}