package com.crbt.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.designsystem.components.ProcessButton
import com.crbt.ui.core.ui.EmailCheck
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.UsernameDetails
import com.example.crbtjetcompose.feature.onboarding.R

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(72.dp))
        Text(
            text = stringResource(id = R.string.feature_onboarding_profile_setup_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        UsernameDetails(
            modifier = modifier,
            onUserProfileResponse = onboardingViewModel::onUserProfileEntered,
            initialFirstName = "",
            initialLastName = "",
        )
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingSheetContainer(
            title = stringResource(id = R.string.feature_onboarding_updates_title),
            subtitle = stringResource(id = R.string.feature_onboarding_updates_subtitle),
            content = {
                EmailCheck(
                    modifier = modifier,
                    onEmailCheckChanged = { /*todo handle with vm*/ },
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProcessButton(
            onClick = {
                onboardingViewModel.saveUserProfile()
                onOnboardingComplete()
            },
            modifier = modifier
                .fillMaxWidth(),
            isEnabled = onboardingViewModel.isNextEnabled
        )
    }
}


@Preview
@Composable
fun UsernameDetailsPreview() {
    UsernameDetails(
        onUserProfileResponse = { _, _, _ -> },
        initialFirstName = "kofi k.",
        initialLastName = "",
    )
}

@Preview
@Composable
fun ProfilePreview() {
    Profile(
        onOnboardingComplete = {},
    )
}