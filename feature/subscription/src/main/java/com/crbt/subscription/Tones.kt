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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.musicService.PlayerState
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.CustomRotatingMorphShape
import com.crbt.ui.core.ui.EmptyContent
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.SearchToolbar
import com.crbt.ui.core.ui.ToneItem
import com.crbt.ui.core.ui.musicPlayer.MusicCard
import com.example.crbtjetcompose.core.model.data.UserCRbtSongResource
import com.example.crbtjetcompose.feature.subscription.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TonesScreen(
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
) {
    val crbtSongsViewModel: CrbtSongsViewModel = hiltViewModel()

    val crbSongsFeed by crbtSongsViewModel.crbtSongsFlow.collectAsStateWithLifecycle()
    val playingNow by crbtSongsViewModel.currentlyPlayingSong.collectAsStateWithLifecycle()
    val playerState by crbtSongsViewModel.playerState.collectAsStateWithLifecycle()


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(playerState) {
        when (playerState) {
            is PlayerState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (playerState as PlayerState.Error).message,
                    duration = SnackbarDuration.Short
                )
            }

            else -> Unit
        }
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

                            when (crbSongsFeed) {
                                is CrbtSongsFeedUiState.Loading -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is CrbtSongsFeedUiState.Success -> {
                                    val crbtSongs =
                                        (crbSongsFeed as CrbtSongsFeedUiState.Success).songs
                                    when (crbtSongs.size) {
                                        0 -> {
                                            EmptyContent(
                                                description = stringResource(id = R.string.feature_subscription_no_songs),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                            )
                                        }

                                        else ->
                                            LazyColumn(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentPadding = PaddingValues(top = 8.dp)
                                            ) {
                                                subscriptionTonesFeed(
                                                    onSubscriptionToneClick = onSubscriptionToneClick,
                                                    onGiftSubscriptionClick = onGiftSubscriptionClick,
                                                    onPlaySong = { song ->
                                                        crbtSongsViewModel.playOrPauseSong(song)
                                                    },
                                                    onPaused = { crbtSongsViewModel.pauseSong() },
                                                    crbtSongs = crbtSongs,
                                                    isPlaying = { song ->
                                                        crbtSongsViewModel.isSongPlaying(song)
                                                    }
                                                )
                                            }
                                    }
                                }

                                is CrbtSongsFeedUiState.Error -> {
                                    EmptyContent(
                                        description = (crbSongsFeed as CrbtSongsFeedUiState.Error).errorMessage,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
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
                onPlay = {
                    crbtSongsViewModel.playOrPauseSong(playingNow!!)
                },
                onPaused = {
                    crbtSongsViewModel.pauseSong()
                },
                onSkipPreviousClick = {
                    crbtSongsViewModel.previousSong()
                },
                onSkipNextClick = {
                    crbtSongsViewModel.nextSong()
                },
                musicTitle = playingNow?.songTitle ?: "",
                musicArtist = playingNow?.artisteName ?: "",
                isPlaying = playerState is PlayerState.Playing,
                musicCoverUrl = playingNow?.profile ?: "",
                modifier = Modifier
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}


fun LazyListScope.subscriptionTonesFeed(
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    onPlaySong: (UserCRbtSongResource) -> Unit,
    onPaused: () -> Unit,
    isPlaying: (UserCRbtSongResource) -> Boolean,
    crbtSongs: List<UserCRbtSongResource>
) {

    items(
        items = crbtSongs,
        key = { it.id }
    ) { song ->
        SubscriptionTone(
            title = song.songTitle,
            subtitle = song.artisteName,
            onSubscriptionToneClick = onSubscriptionToneClick,
            onGiftSubscriptionClick = onGiftSubscriptionClick,
            toneId = song.id,
            imageUrl = song.profile,
            showDivider = crbtSongs.indexOf(song) != crbtSongs.size - 1,
            onPlaySong = { onPlaySong(song) },
            onPaused = onPaused,
            isPlaying = isPlaying(song)
        )
    }
}

@Composable
fun SubscriptionTone(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    imageUrl: String,
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    toneId: String,
    showDivider: Boolean = true,
    onPlaySong: () -> Unit,
    onPaused: () -> Unit,
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
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                onPaused()
                            } else {
                                onPlaySong()
                            }
                        }
                    ) {
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


@ThemePreviews
@Composable
fun SubscriptionTonePreview() {
    CrbtTheme {
        SubscriptionTone(
            title = "Title",
            subtitle = "Subtitle",
            imageUrl = "",
            toneId = "",
            onSubscriptionToneClick = {},
            onGiftSubscriptionClick = {},
            onPlaySong = {},
            onPaused = {},
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