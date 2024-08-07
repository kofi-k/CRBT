package com.crbt.ui.core.ui.otp

import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.common.core.common.result.Result
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.example.crbtjetcompose.core.ui.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    otpValue: String,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onOtpModified: (String, Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        OtpInputField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            otpText = otpValue,
            shouldCursorBlink = false,
            onOtpModified = onOtpModified,
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Icon(imageVector = CrbtIcons.OtpCode, contentDescription = "resend otp")
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = stringResource(
                        id = R.string.core_ui_request_new_code,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(
                        id = R.string.core_ui_otp_resend_timer,
                        "60 seconds",
                    ), // TODO: Add countdown timer here
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Icon(imageVector = CrbtIcons.Help, contentDescription = "otp help")
            Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.core_ui_need_help,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
        }
    }

@RequiresApi(VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    CrbtTheme {
        OtpScreen(
            otpValue = "1234",
            onOtpModified = { _, _ -> },
        )
    }
}
