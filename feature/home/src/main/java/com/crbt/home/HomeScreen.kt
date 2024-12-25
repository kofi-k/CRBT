package com.crbt.home

import PullToRefreshContent
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.data.core.data.repository.CrbtAdsUiState
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.CHECK_BALANCE_USSD
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.EmptyContent
import com.crbt.ui.core.ui.PermissionRequestComposable
import com.crbt.ui.core.ui.UssdResponseDialog
import com.crbt.ui.core.ui.musicPlayer.CrbtTonesViewModel
import com.crbt.ui.core.ui.rememberDominantColorWithReadableText
import com.example.crbtjetcompose.feature.home.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigateToSubscription: () -> Unit,
    onNavigateToTopUp: () -> Unit = {},
    crbtTonesViewModel: CrbtTonesViewModel,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val ussdUiState by viewModel.ussdState.collectAsStateWithLifecycle()
    val userDataUiState by viewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val crbtAdsUiState by viewModel.crbtAdsUiState.collectAsStateWithLifecycle()
    val tonesUiState by crbtTonesViewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    var showDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val isInitialized = rememberSaveable { mutableStateOf(false) }

    if (!isInitialized.value) {
        LaunchedEffect(key1 = Unit) {
            crbtTonesViewModel.onEvent(TonesPlayerEvent.FetchSong)
            isInitialized.value = true
        }
    }

    PermissionRequestComposable(
        onPermissionsGranted = {}
    )


    PullToRefreshContent(
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState,
                contentPadding = PaddingValues(
                    vertical = 24.dp,
                    horizontal = 0.dp
                )
            ) {
                item {
                    UserBalanceCard(
                        onNavigateToTopUp = onNavigateToTopUp,
                        onRefresh = {
                            viewModel.runNewUssdCode(
                                ussdCode = CHECK_BALANCE_USSD,
                                onSuccess = {
                                    showDialog = true
                                },
                                onError = {
                                    showDialog = true
                                },
                                activity = context as Activity
                            )

                        },
                        isRefreshing = ussdUiState is UssdUiState.Loading,
                        userPreferenceUiState = userDataUiState
                    )
                }

                item {
                    CrbtAds(crbtAdsUiState)
                }

                with(tonesUiState) {
                    when (errorMessage != null) {
                        true -> item {
                            EmptyContent(
                                description = errorMessage!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 42.dp, horizontal = 24.dp),
                                reloadContent = {
                                    ProcessButton(
                                        onClick = {
                                            viewModel.reloadHome()
                                            crbtTonesViewModel.onEvent(
                                                TonesPlayerEvent.FetchSong
                                            )
                                        },
                                        text = stringResource(id = com.example.crbtjetcompose.core.ui.R.string.core_ui_reload_button),
                                        colors = ButtonDefaults.textButtonColors()
                                    )
                                }
                            )
                        }

                        else -> when (loading) {
                            true -> Unit
                            /*item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }*/

                            else -> when (
                                !songs.isNullOrEmpty() && homeResource != null
                            ) {
                                true -> {
                                    val songResource = homeResource!!.popularTodaySongs
                                    val latestMusic = homeResource!!.latestSong

                                    item {
                                        if (latestMusic.id.toIntOrNull() != null && latestMusic.profile.isNotBlank()) {
                                            LatestMusicCard(
                                                artist = latestMusic.artisteName,
                                                title = latestMusic.songTitle,
                                                backgroundUrl = latestMusic.profile,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .defaultMinSize(minHeight = 180.dp)
                                                    .padding(horizontal = 16.dp)
                                                    .clip(MaterialTheme.shapes.large)
                                                    .background(
                                                        MaterialTheme.colorScheme.surface.copy(
                                                            alpha = 0.5f
                                                        )
                                                    )
                                                    .clickable(
                                                        onClick = {
                                                            crbtTonesViewModel.onEvent(
                                                                TonesPlayerEvent.OnSongSelected(
                                                                    latestMusic
                                                                )
                                                            )
                                                            crbtTonesViewModel.onEvent(
                                                                TonesPlayerEvent.PlaySong
                                                            )
                                                        },
                                                        role = Role.Button,
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = LocalIndication.current,
                                                    )
                                            )
                                        }
                                    }

                                    item {
                                        PopularTodayTabLayout(
                                            navigateToSubscriptions = { toneId ->
                                                songResource.find {
                                                    it.id == toneId
                                                }.also { crbtSongResource ->
                                                    crbtSongResource?.let {
                                                        crbtTonesViewModel.onEvent(
                                                            TonesPlayerEvent.OnSongSelected(it)
                                                        )
                                                        crbtTonesViewModel.onEvent(TonesPlayerEvent.PlaySong)
                                                    }
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            crbSongsFeed = CrbtSongsFeedUiState.Success(
                                                songResource,
                                                null
                                            ),
                                        )
                                    }

                                    item {
                                        RecentSubscription(
                                            navigateToSubscription = navigateToSubscription,
                                            userSubscriptions = tonesUiState.homeResource?.currentUserCrbtSubscription,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        )
                                    }
                                }

                                else -> item {
                                    EmptyContent(
                                        description = stringResource(id = R.string.feature_home_no_songs),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 42.dp, horizontal = 24.dp),
                                        reloadContent = {
                                            ProcessButton(
                                                onClick = {
                                                    crbtTonesViewModel.onEvent(
                                                        TonesPlayerEvent.FetchSong
                                                    )
                                                },
                                                text = stringResource(id = com.example.crbtjetcompose.core.ui.R.string.core_ui_reload_button),
                                                colors = ButtonDefaults.textButtonColors()
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        onRefresh = {
            viewModel.reloadHome()
            crbtTonesViewModel.onEvent(TonesPlayerEvent.FetchSong)
        },
        isRefreshing = crbtAdsUiState is CrbtAdsUiState.Loading || tonesUiState.loading == true
    )

    if (showDialog) {
        UssdResponseDialog(
            onDismiss = {
                showDialog = false
            },
            ussdUiState = ussdUiState,
            crbtUssdType = CrbtUssdType.BALANCE_CHECK
        )
    }
}

@Composable
internal fun UserBalanceCard(
    onNavigateToTopUp: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean = false,
    userPreferenceUiState: UserPreferenceUiState
) {
    Card(
        onClick = onNavigateToTopUp,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.linearGradient(CustomGradientColors),
                shape = MaterialTheme.shapes.large,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                when (userPreferenceUiState) {
                    is UserPreferenceUiState.Success ->
                        Text(
                            text = stringResource(
                                id = R.string.feature_home_balance,
                                userPreferenceUiState.userData.currentBalance
                            ),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.White
                        )

                    else -> CircularProgressIndicator()
                }

                Text(
                    text = stringResource(id = R.string.feature_home_balance_subtitle),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(
                onClick = onRefresh
            ) {
                when (isRefreshing) {
                    true -> CircularProgressIndicator(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        color = Color.White
                    )

                    else ->
                        Icon(
                            imageVector = CrbtIcons.Refresh,
                            contentDescription = CrbtIcons.Refresh.name,
                            modifier = Modifier,
                            tint = Color.White
                        )
                }

            }
        }
    }
}

@Composable
fun LatestMusicCard(
    artist: String,
    title: String,
    backgroundUrl: String?,
    modifier: Modifier = Modifier
) {
    val (_, textColor) = rememberDominantColorWithReadableText(backgroundUrl)

    Box(
        modifier = modifier
    ) {
        DynamicAsyncImage(
            imageUrl = backgroundUrl,
            imageRes = com.example.crbtjetcompose.core.ui.R.drawable.core_ui_paps_image,
            modifier = Modifier
                .matchParentSize()
                .clip(MaterialTheme.shapes.large)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        )

        Text(
            text = stringResource(id = R.string.feature_home_latest_music),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = artist,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview
@Composable
fun PreviewLatestMusicCard() {
    CrbtTheme {
        LatestMusicCard(
            artist = "Artist",
            title = "Title",
            backgroundUrl = "",
        )
    }
}