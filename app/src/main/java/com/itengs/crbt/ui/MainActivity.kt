package com.itengs.crbt.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crbt.data.core.data.musicService.MusicService
import com.crbt.data.core.data.repository.CrbtPreferencesRepository
import com.crbt.data.core.data.repository.UserManager
import com.crbt.data.core.data.util.NetworkMonitor
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.musicPlayer.CrbtTonesViewModel
import com.crbt.ui.core.ui.musicPlayer.SharedCrbtMusicPlayerViewModel
import com.example.crbtjetcompose.core.analytics.AnalyticsHelper
import com.example.crbtjetcompose.core.analytics.LocalAnalyticsHelper
import com.kofik.freeatudemy.core.model.data.DarkThemeConfig
import com.kofik.freeatudemy.core.model.data.ThemeBrand
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var crbtPreferencesRepository: CrbtPreferencesRepository

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainActivityViewModel by viewModels()
    private val sharedCrbtMusicPlayerViewModel: SharedCrbtMusicPlayerViewModel by viewModels()
    private val crbtTonesViewModel: CrbtTonesViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen =
            installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { state ->
                    uiState = state
                }.collect()
            }
        }

        lifecycleScope.launch {
            crbtPreferencesRepository.userPreferencesData
                .filterNotNull()
                .distinctUntilChanged()
                .collect { preferences ->
                    preferences.languageCode.let { code ->
                        val locale = Locale(code)
                        val localeList = LocaleListCompat.create(locale)

                        AppCompatDelegate.setApplicationLocales(localeList)

                        @Suppress("DEPRECATION")
                        resources.updateConfiguration(
                            resources.configuration.apply {
                                setLocales(LocaleList(locale))
                            },
                            resources.displayMetrics
                        )
                    }
                }
        }


        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            uiState is MainActivityUiState.Loading
        }

        enableEdgeToEdge()


        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)


            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }


            CrbtTheme {
                CompositionLocalProvider(
                    LocalAnalyticsHelper provides analyticsHelper,
                ) {
                    val appState = rememberCrbtAppState(
                        networkMonitor = networkMonitor,
                        userRepository = crbtPreferencesRepository,
                        userManager = userManager
                    )

                    CrbtTheme(
                        darkTheme = darkTheme,
                        androidTheme = shouldUseAndroidTheme(uiState),
                        disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                    ) {
                        CrbtApp(
                            appState = appState,
                            sharedCrbtMusicPlayerViewModel = sharedCrbtMusicPlayerViewModel,
                            crbtTonesViewModel = crbtTonesViewModel,
                            mainActivityViewModel = mainActivityViewModel
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedCrbtMusicPlayerViewModel.destroyMediaController()
        stopService(Intent(this, MusicService::class.java))
    }

}


/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

/**
 * Returns `true` if the Android theme should be used, as a function of the [uiState].
 */
@Composable
private fun shouldUseAndroidTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> !uiState.userData.useDynamicColor
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)