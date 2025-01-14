package com.crbt.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Light default theme color scheme
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


val LightAndroidColorScheme = lightColorScheme(
    primary = primaryLightAndroid,
    onPrimary = onPrimaryLightAndroid,
    primaryContainer = primaryContainerLightAndroid,
    onPrimaryContainer = onPrimaryContainerLightAndroid,
    secondary = secondaryLightAndroid,
    onSecondary = onSecondaryLightAndroid,
    secondaryContainer = secondaryContainerLightAndroid,
    onSecondaryContainer = onSecondaryContainerLightAndroid,
    tertiary = tertiaryLightAndroid,
    onTertiary = onTertiaryLightAndroid,
    tertiaryContainer = tertiaryContainerLightAndroid,
    onTertiaryContainer = onTertiaryContainerLightAndroid,
    error = errorLightAndroid,
    onError = onErrorLightAndroid,
    errorContainer = errorContainerLightAndroid,
    onErrorContainer = onErrorContainerLightAndroid,
    background = backgroundLightAndroid,
    onBackground = onBackgroundLightAndroid,
    surface = surfaceLightAndroid,
    onSurface = onSurfaceLightAndroid,
    surfaceVariant = surfaceVariantLightAndroid,
    onSurfaceVariant = onSurfaceVariantLightAndroid,
    outline = outlineLightAndroid,
    outlineVariant = outlineVariantLightAndroid,
    scrim = scrimLightAndroid,
    inverseSurface = inverseSurfaceLightAndroid,
    inverseOnSurface = inverseOnSurfaceLightAndroid,
    inversePrimary = inversePrimaryLightAndroid,
    surfaceDim = surfaceDimLightAndroid,
    surfaceBright = surfaceBrightLightAndroid,
    surfaceContainerLowest = surfaceContainerLowestLightAndroid,
    surfaceContainerLow = surfaceContainerLowLightAndroid,
    surfaceContainer = surfaceContainerLightAndroid,
    surfaceContainerHigh = surfaceContainerHighLightAndroid,
    surfaceContainerHighest = surfaceContainerHighestLightAndroid,
)

/**
 * Dark Android theme color scheme
 */
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = primaryDarkAndroid,
    onPrimary = onPrimaryDarkAndroid,
    primaryContainer = primaryContainerDarkAndroid,
    onPrimaryContainer = onPrimaryContainerDarkAndroid,
    secondary = secondaryDarkAndroid,
    onSecondary = onSecondaryDarkAndroid,
    secondaryContainer = secondaryContainerDarkAndroid,
    onSecondaryContainer = onSecondaryContainerDarkAndroid,
    tertiary = tertiaryDarkAndroid,
    onTertiary = onTertiaryDarkAndroid,
    tertiaryContainer = tertiaryContainerDarkAndroid,
    onTertiaryContainer = onTertiaryContainerDarkAndroid,
    error = errorDarkAndroid,
    onError = onErrorDarkAndroid,
    errorContainer = errorContainerDarkAndroid,
    onErrorContainer = onErrorContainerDarkAndroid,
    background = backgroundDarkAndroid,
    onBackground = onBackgroundDarkAndroid,
    surface = surfaceDarkAndroid,
    onSurface = onSurfaceDarkAndroid,
    surfaceVariant = surfaceVariantDarkAndroid,
    onSurfaceVariant = onSurfaceVariantDarkAndroid,
    outline = outlineDarkAndroid,
    outlineVariant = outlineVariantDarkAndroid,
    scrim = scrimDarkAndroid,
    inverseSurface = inverseSurfaceDarkAndroid,
    inverseOnSurface = inverseOnSurfaceDarkAndroid,
    inversePrimary = inversePrimaryDarkAndroid,
    surfaceDim = surfaceDimDarkAndroid,
    surfaceBright = surfaceBrightDarkAndroid,
    surfaceContainerLowest = surfaceContainerLowestDarkAndroid,
    surfaceContainerLow = surfaceContainerLowDarkAndroid,
    surfaceContainer = surfaceContainerDarkAndroid,
    surfaceContainerHigh = surfaceContainerHighDarkAndroid,
    surfaceContainerHighest = surfaceContainerHighestDarkAndroid,
)


const val stronglyDeemphasizedAlpha = 0.6f
const val slightlyDeemphasizedAlpha = 0.87f
const val extremelyDeemphasizedAlpha = 0.32f

/**
 *  Gradient colors
 * */
val CustomGradientColors = listOf(
    Color(0xFF9E82F0),
    Color(0xFF42A5F5)
)

val AndroidGradientColors = GradientColors(
    top = Color(0xFF8AD6B6),
    bottom = Color(0xFF1A6B51),
//    bottom = Color(0xFF42A5F5),
)

/**
 * Dark Android gradient colors
 */
val DarkAndroidGradientColors = GradientColors(container = surfaceContainerLowDark)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme =
    BackgroundTheme(color = surfaceContainerLowDark, tonalElevation = 2.dp)


val LightAndroidBackgroundTheme = BackgroundTheme(color = surfaceLightMediumContrast)

/**
 * CRBT theme.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 */
@Composable
fun CrbtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }

    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )

    val gradientColors = when (darkTheme) {
        true -> DarkAndroidGradientColors
        else -> emptyGradientColors
    }

    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }

    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CrbtTypography,
            content = content,
            shapes = Shapes
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
