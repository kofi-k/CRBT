package com.crbt.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.supportsDynamicTheming
import com.itengs.crbt.feature.profile.R
import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand


object ListOfThemes {
    val themes = listOf(
        DarkThemeConfig.FOLLOW_SYSTEM to CrbtIcons.SystemDefault,
        DarkThemeConfig.DARK to CrbtIcons.DarkMode,
        DarkThemeConfig.LIGHT to CrbtIcons.LightMode,
    )
}

@Composable
fun ThemeSettingsScreen(
    updateThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    updateDarkThemeConfig: (
        darkThemeConfig: DarkThemeConfig
    ) -> Unit,
    updateDynamicColorPreference: (
        useDynamicColor: Boolean
    ) -> Unit,
    themeSettings: UserEditableSettings
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = CrbtIcons.ThemePalette,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.feature_profile_theme),
            )
        }

        SurfaceCard(content = {
            DefaultThemeSelection(
                settings = themeSettings,
                usesDynamicColor = themeSettings.useDynamicColor,
                supportDynamicColor = supportsDynamicTheming(),
                onChangeDynamicColorPreference = updateDynamicColorPreference,
                onDefaultCheckedChange = updateThemeBrand,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            if (themeSettings.brand == ThemeBrand.DEFAULT) {
                                updateThemeBrand(ThemeBrand.ANDROID)
                            } else {
                                updateThemeBrand(ThemeBrand.DEFAULT)
                            }
                        }
                    ),
            )
        })
        DarkModeSelection(
            onChangeDarkThemeConfig = updateDarkThemeConfig,
            settings = themeSettings
        )

    }
}

@Composable
fun DefaultThemeSelection(
    modifier: Modifier = Modifier,
    settings: UserEditableSettings,
    usesDynamicColor: Boolean,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onDefaultCheckedChange: (themeBrand: ThemeBrand) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.feature_profile_brand_default))
            Switch(
                checked = settings.brand == ThemeBrand.DEFAULT,
                onCheckedChange = {
                    if (it) {
                        onDefaultCheckedChange(ThemeBrand.DEFAULT)
                    } else {
                        onDefaultCheckedChange(ThemeBrand.ANDROID)
                    }
                },
                thumbContent = {
                    if (settings.brand == ThemeBrand.DEFAULT) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            )
        }

        AnimatedVisibility(visible = (settings.brand == ThemeBrand.DEFAULT) && supportDynamicColor) {
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
            ) {
                SettingsDialogSectionTitle(text = stringResource(R.string.feature_profile_dynamic_color_preference))
                Column(Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.feature_profile_dynamic_color_yes),
                        selected = usesDynamicColor,
                        onClick = { onChangeDynamicColorPreference(true) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.feature_profile_dynamic_color_no),
                        selected = !usesDynamicColor,
                        onClick = { onChangeDynamicColorPreference(false) },
                    )
                }
            }
        }
    }
}

@Composable
fun DarkModeSelection(
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    settings: UserEditableSettings,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.feature_profile_dark_mode_preference),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            ListOfThemes.themes.forEach { theme ->
                val darkThemeText = when (theme.first) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> stringResource(R.string.feature_profile_dark_mode_config_system_default)
                    DarkThemeConfig.DARK -> stringResource(R.string.feature_profile_dark_mode_config_dark)
                    DarkThemeConfig.LIGHT -> stringResource(R.string.feature_profile_dark_mode_config_light)
                }
                FilterChip(
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                    selected = theme.first == settings.darkThemeConfig,
                    onClick = {
                        onChangeDarkThemeConfig(theme.first)
                    },
                    leadingIcon = {
                        if (theme.first == settings.darkThemeConfig)
                            Icon(
                                imageVector = theme.second,
                                contentDescription = "check image vector"
                            )
                    },
                    label = { Text(text = darkThemeText) },
                    colors = FilterChipDefaults.filterChipColors()
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}


@Composable
fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}