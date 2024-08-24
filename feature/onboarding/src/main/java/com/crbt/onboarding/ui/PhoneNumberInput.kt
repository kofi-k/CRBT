package com.crbt.onboarding.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.PhoneEntryScreen
import com.example.crbtjetcompose.feature.onboarding.R

@Composable
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    onPhoneNumberChanged: (String, Boolean) -> Unit,
) {
    OnboardingSheetContainer(
        modifier = modifier,
        title = stringResource(id = R.string.feature_onboarding_welcome_title),
        subtitle = stringResource(id = R.string.feature_onboarding_phone_number_entry_message),
        content = {
            PhoneEntryScreen { phoneNumber, isValid ->
                onPhoneNumberChanged(phoneNumber, isValid)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.feature_onboarding_privacy_policy_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PhoneNumberInputPreview() {
    CrbtTheme {
        PhoneNumberInput { _, _ -> }
    }
}