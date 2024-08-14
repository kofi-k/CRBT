package com.crbt.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.CRBTLanguage
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.CustomInputButton
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.example.crbtjetcompose.feature.onboarding.R


@Composable
fun LanguageSelection(
    modifier: Modifier = Modifier,
    onLanguageSelected: (CRBTLanguage) -> Unit,
    selectedLanguage: CRBTLanguage
) {
    OnboardingSheetContainer(
        titleRes = R.string.feature_onboarding_language_selection_title,
        subtitleRes = R.string.feature_onboarding_language_selection_subtitle,
        content = {
            LanguageSelectionMenu(
                modifier = modifier,
                onLanguageSelected = onLanguageSelected,
                selectedLanguage = selectedLanguage
            )
        }
    )
}

@Composable
fun LanguageSelectionMenu(
    modifier: Modifier = Modifier,
    onLanguageSelected: (CRBTLanguage) -> Unit,
    selectedLanguage: CRBTLanguage
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")


    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.outlineVariant.copy(
            alpha = stronglyDeemphasizedAlpha
        ),
    ) {
        Column {
            CustomInputButton(
                text = stringResource(id = selectedLanguage.languageResValue),
                onClick = {
                    expanded = !expanded
                },
                trailingIcon = {
                    Icon(
                        imageVector = CrbtIcons.ArrowRight,
                        contentDescription = CrbtIcons.ArrowRight.name,
                        modifier = Modifier.rotate(rotateIcon),
                    )
                },
                elevation = if (expanded) null else ButtonDefaults.buttonElevation(),
            )
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                ) {
                    CRBTLanguage.entries.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(stringResource(id = language.languageResValue)) },
                            onClick = {
                                onLanguageSelected(language)
                                expanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LanguageSelectionPreview() {
    LanguageSelection(
        onLanguageSelected = {},
        selectedLanguage = CRBTLanguage.ENGLISH
    )
}

@Preview
@Composable
fun LanguageSelectionMenuPreview() {
    LanguageSelectionMenu(
        onLanguageSelected = {},
        selectedLanguage = CRBTLanguage.ENGLISH
    )
}