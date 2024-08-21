package com.crbt.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.CRBTSettingsData
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.feature.profile.R

@Composable
fun LanguageSettings(
    onLanguageCheckChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")
    var selectedLanguage by remember { mutableStateOf(CRBTSettingsData.languages.first().code) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListCard(
            onClick = {
                expanded = !expanded
            },
            headlineText = stringResource(id = R.string.feature_profile_language),
            subText = stringResource(id = R.string.feature_profile_language_description),
            leadingContentIcon = CrbtIcons.Language,
            trailingContent = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = CrbtIcons.ArrowRight,
                        contentDescription = CrbtIcons.ArrowRight.name,
                        modifier = Modifier.rotate(rotateIcon)
                    )
                }
            }
        )
        if (expanded) {
            Column {
                CRBTSettingsData.languages.forEach { language ->
                    ListCard(
                        onClick = {
                            onLanguageCheckChange(language.code)
                            selectedLanguage = language.code
                        },
                        headlineText = stringResource(id = language.name),
                        leadingContentIcon = CrbtIcons.Language,
                        leadingContent = {},
                        trailingContent = {
                            RadioButton(
                                selected = selectedLanguage == language.code,
                                onClick = {
                                    onLanguageCheckChange(language.code)
                                    selectedLanguage = language.code
                                    expanded = false
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp)
                    )
                }
            }
        }
    }

}