package com.crbt.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.bodyFontFamily
import com.example.crbtjetcompose.feature.onboarding.R

@Composable
fun OnboardingFinishedScreen(
    navigateToHome: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DynamicAsyncImage(
            imageUrl = null,
            imageRes = R.drawable.onboarding_jorge,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(72.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = bodyFontFamily
                        ).toSpanStyle()
                    ) {
                        append(stringResource(id = R.string.feature_onboarding_whats_trending).uppercase())
                        append("\n\n")
                        append(stringResource(id = R.string.feature_onboarding_trending).uppercase())

                    }
                    append("\n")
                    append("\n")
                    withStyle(
                        MaterialTheme.typography.titleLarge
                            .copy(color = Color.White)
                            .toSpanStyle(),
                    ) {
                        append(stringResource(id = R.string.feature_onboarding_trending_subtitle))
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProcessButton(
                onClick = navigateToHome,
                text = stringResource(id = R.string.feature_onboarding_explore_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            )
        }
    }
}

@Preview
@Composable
fun OnboardingFinishedScreenPreview() {
    CrbtTheme {
        OnboardingFinishedScreen(navigateToHome = {})
    }
}