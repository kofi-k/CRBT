package com.crbt.ui.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.itengs.crbt.core.ui.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    description: String,
    isSystemUnderMaintenance: Boolean = false,
    reloadContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {


        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                if (isSystemUnderMaintenance) {
                    R.raw.core_ui_maintenance_anim
                } else {
                    R.raw.core_ui_lottie_error
                }
            ),
            cacheKey = "lottie_error"
        )

        val animationState by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { animationState },
            modifier = Modifier
                .size(200.dp)
        )
    },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        errorContent()
        Text(
            text = if (isSystemUnderMaintenance) {
                stringResource(id = R.string.core_ui_system_under_maintenance)
            } else {
                description
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        reloadContent()
    }

}

@Preview(showBackground = true)
@Composable
fun EmptyContentPreview() {
    CrbtTheme {
        EmptyContent(
            description = "No content available",
            isSystemUnderMaintenance = true
        )
    }
}