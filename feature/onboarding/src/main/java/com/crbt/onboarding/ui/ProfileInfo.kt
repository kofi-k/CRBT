package com.crbt.onboarding.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.data.core.data.repository.UpdateUserInfoUiState
import com.crbt.designsystem.components.ProcessButton
import com.crbt.ui.core.ui.EmailCheck
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.UsernameDetails
import com.crbt.ui.core.ui.validationStates.isValidEmail
import com.itengs.crbt.feature.onboarding.R
import kotlinx.coroutines.launch


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onOnboardingComplete: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
) {
    val updateUserInfoUiState = onboardingViewModel.userInfoUiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var checked by remember {
        mutableStateOf(false)
    }
    var userEmail by remember {
        mutableStateOf("")
    }

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
                    onEmailCheckChanged = {
                        checked = it
                        if (!checked) {
                            userEmail = ""
                        }
                    },
                    checked = checked,
                    onEmailChanged = { email, _ ->
                        userEmail = email
                    },
                    userEmailAddress = userEmail
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProcessButton(
            onClick = {
                onboardingViewModel.updateUserProfileInfo(
                    onSuccessfulUpdate = {
                        onOnboardingComplete()
                    },
                    onFailedUpdate = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                it,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    email = userEmail
                )
            },
            modifier = modifier
                .fillMaxWidth(),
            isEnabled = if (checked) {
                onboardingViewModel.isNextEnabled && userEmail.isValidEmail()
            } else {
                onboardingViewModel.isNextEnabled
            },
            isProcessing = updateUserInfoUiState is UpdateUserInfoUiState.Loading
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
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