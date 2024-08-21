package com.crbt.onboarding.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.CRBTSettingsData
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.CustomInputButton
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.example.crbtjetcompose.feature.onboarding.R


@Composable
fun LanguageSelection(
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String
) {
    OnboardingSheetContainer(
        title = stringResource(id = R.string.feature_onboarding_language_selection_title),
        subtitle = stringResource(id = R.string.feature_onboarding_language_selection_subtitle),
        content = {
            LanguageSelectionMenu(
                onLanguageSelected = onLanguageSelected,
                selectedLanguage = selectedLanguage
            )
        }
    )
}

@Composable
fun LanguageSelectionMenu(
    modifier: Modifier = Modifier,
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")


    SurfaceCard(
        modifier = modifier
            .wrapContentHeight()
            .animateContentSize(),
        color = MaterialTheme.colorScheme.surfaceBright,
        content = {
            Column {
                CustomInputButton(
                    text = stringResource(
                        id = CRBTSettingsData.languages.first { it.code == selectedLanguage }.name
                    ),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                )
                if (expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        CRBTSettingsData.languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(stringResource(id = language.name)) },
                                onClick = {
                                    onLanguageSelected(language.code)
                                    expanded = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .padding(start = 32.dp),
                            )
                        }
                    }
                }
            }
        }
    )
}


@ThemePreviews
@Composable
fun LanguageSelectionMenuPreview() {
    CrbtTheme {
        LanguageSelection(
            onLanguageSelected = {},
            selectedLanguage = CRBTSettingsData.languages.first().code
        )
    }
}