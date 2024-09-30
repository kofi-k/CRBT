package com.crbt.subscription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.DummyTones.tones
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.CustomRotatingMorphShape
import com.crbt.ui.core.ui.SearchToolbar
import com.crbt.ui.core.ui.ToneItem
import com.crbt.ui.core.ui.musicPlayer.MusicCard
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import com.example.crbtjetcompose.feature.subscription.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TonesScreen(
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
) {
    val viewModel: MusicPlayerViewModel = hiltViewModel()
    val subscriptionViewModel: SubscriptionViewModel = hiltViewModel()
    val musicFiles by viewModel.musicFiles.collectAsStateWithLifecycle()
    val playingNow by viewModel.currentTrack.collectAsStateWithLifecycle()
    var toneIndex by remember {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        subscriptionViewModel.checkPermissions(
            context,
            onGranted = { viewModel.loadMusicFiles() }
        )
    }

    Column(
        modifier = Modifier
    ) {
        var searchQuery by remember { mutableStateOf("") }

        SearchToolbar(
            searchQuery = searchQuery,
            onSearchQueryChanged = {
                searchQuery = it
            },
            onSearchTriggered = {
                //TODO
            },
            onBackClick = { /*TODO*/ },
            showNavigationIcon = false,
            modifier = Modifier
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SurfaceCard(
                    content = {
                        Column(modifier = Modifier) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                TextButton(
                                    onClick = { /*TODO show language selection options menu */ },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(text = stringResource(id = R.string.feature_subscription_laguage_change_title))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = CrbtIcons.Language,
                                        contentDescription = null
                                    )
                                }

                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = CrbtIcons.FilterList,
                                        contentDescription = null
                                    )
                                }

                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                            ) {
                                subscriptionTonesFeed(
                                    onSubscriptionToneClick = onSubscriptionToneClick,
                                    onGiftSubscriptionClick = onGiftSubscriptionClick,
                                    subscriptionTonesUiState = SubscriptionTonesUiState.Success(
                                        tones
                                    ),
                                    onPlayPauseClick = {
                                        viewModel.playTrack(it.toInt())
//                                        viewModel.playPause()
                                        toneIndex = it.toInt()
                                    },
                                    isPlaying = musicFiles.indexOf(playingNow) == toneIndex
                                )
                            }
                        }
                    },
                    modifier = Modifier
                )
            }

        }
    }
    if (playingNow != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            MusicCard(
                onPlayPauseClick = {
                    viewModel.playPause()
                },
                onSkipPreviousClick = {
                    viewModel.next()
                },
                onSkipNextClick = {
                    viewModel.next()
                },
                musicTitle = tones[toneIndex].songTitle,
                musicArtist = tones[toneIndex].artisteName,
                isPlaying = musicFiles.indexOf(playingNow) == toneIndex,
                musicCoverUrl = tones[toneIndex].profile,
                modifier = Modifier
            )
        }
    }
}


fun LazyListScope.subscriptionTonesFeed(
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    subscriptionTonesUiState: SubscriptionTonesUiState,
    onPlayPauseClick: (String) -> Unit,
    isPlaying: Boolean
) {
    when (subscriptionTonesUiState) {
        is SubscriptionTonesUiState.Loading -> Unit
        is SubscriptionTonesUiState.Success -> {
            items(
                items = tones,
                key = { it.id }
            ) { tone ->
                SubscriptionTone(
                    title = tone.songTitle,
                    subtitle = tone.artisteName,
                    onSubscriptionToneClick = onSubscriptionToneClick,
                    onGiftSubscriptionClick = onGiftSubscriptionClick,
                    toneId = tone.id,
                    imageUrl = tone.profile,
                    showDivider = tones.indexOf(tone) != tones.size - 1,
                    onPlayPauseClick = { onPlayPauseClick(tones.indexOf(tone).toString()) },
                    isPlaying = isPlaying
                )
            }
        }

        is SubscriptionTonesUiState.Error -> {
            item {
                Text(text = subscriptionTonesUiState.message)
            }
        }
    }
}

@Composable
fun SubscriptionTone(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    imageUrl: String? = tones[0].profile,
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    toneId: String,
    showDivider: Boolean = true,
    onPlayPauseClick: () -> Unit,
    isPlaying: Boolean
) {
    val shapeA = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            rounding = CornerRounding(0.2f),
            innerRadius = 0.85f
        )
    }
    val shapeB = remember {
        RoundedPolygon(
            12,
            rounding = CornerRounding(0.2f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    var expanded by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMorphProgress"
    )
    val animatedRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMorphProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(modifier)
    ) {
        ToneItem(
            title = title,
            subtitle = subtitle,
            trailingContent = {
                Row {
                    IconButton(onClick = onPlayPauseClick) {
                        Icon(
                            if (isPlaying) CrbtIcons.PauseCircle else CrbtIcons.Play,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            CrbtIcons.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            },
            imageUrl = imageUrl,
            colors = ListItemDefaults.colors(),
            modifier = Modifier
                .clickable(
                    onClick = { onSubscriptionToneClick(toneId) },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ),
            imageModifier = Modifier
                .clip(
                    CustomRotatingMorphShape(
                        morph = morph,
                        percentage = animatedProgress.value,
                        rotation = animatedRotation.value
                    )
                )
        )
        if (expanded) {
            Column(
                modifier = Modifier
                    .padding(start = 42.dp)
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.feature_subscription_subscribe_gift_title),
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = { onGiftSubscriptionClick(toneId) }) {
                            Icon(imageVector = CrbtIcons.ArrowRight, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGiftSubscriptionClick(toneId) }
                )
            }
            if (showDivider) HorizontalDivider()
        }
    }
}

/**
 * A sealed hierarchy describing the state of the feed of subscription tones.
 */
sealed interface SubscriptionTonesUiState {
    data object Loading : SubscriptionTonesUiState
    data class Success(val tones: List<CrbtSongResource>) : SubscriptionTonesUiState
    data class Error(val message: String) : SubscriptionTonesUiState
}

@ThemePreviews
@Composable
fun SubscriptionTonePreview() {
    CrbtTheme {
        SubscriptionTone(
            title = "Title",
            subtitle = "Subtitle",
            imageUrl = null,
            toneId = "",
            onSubscriptionToneClick = {},
            onGiftSubscriptionClick = {},
            onPlayPauseClick = {},
            isPlaying = false
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ThemePreviews
@Composable
fun TonesPreview() {
    CrbtTheme {
        TonesScreen(
            onSubscriptionToneClick = {},
            onGiftSubscriptionClick = {}
        )
    }
}