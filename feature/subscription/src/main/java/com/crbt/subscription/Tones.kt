package com.crbt.subscription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.MusicControllerUiState
import com.crbt.data.core.data.PlayerState
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.CustomRotatingMorphShape
import com.crbt.ui.core.ui.EmptyContent
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
    musicControllerUiState: MusicControllerUiState,
    crbtTonesViewModel: CrbtTonesViewModel = hiltViewModel(),
) {
    val currentSong = musicControllerUiState.currentSong
    val tonesUiState = crbtTonesViewModel.tonesUiState

    val isInitialized = rememberSaveable { mutableStateOf(false) }

    if (!isInitialized.value) {
        LaunchedEffect(key1 = Unit) {
            crbtTonesViewModel.onEvent(TonesPlayerEvent.FetchSong)
            isInitialized.value = true
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

                            with(tonesUiState) {
                                when {
                                    loading == true -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 16.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    loading == false && songs != null -> {
                                        when (songs.isEmpty()) {
                                            true -> {
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
                                                        onEvent = crbtTonesViewModel::onEvent,
                                                        crbtSongs = songs,
                                                        currentSong = tonesUiState.selectedSong,
                                                        isPlaying = musicControllerUiState.playerState == PlayerState.PLAYING
                                                    )
                                                }
                                        }
                                    }

                                    errorMessage != null -> {
                                        EmptyContent(
                                            description = errorMessage,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            reloadContent = {
                                                ProcessButton(
                                                    onClick = { crbtTonesViewModel.reload() },
                                                    isProcessing = tonesUiState.loading == true,
                                                    text = stringResource(id = R.string.feature_subscription_reload),
                                                    colors = ButtonDefaults.textButtonColors()
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .animateContentSize()
                )
            }
        }
    }

    AnimatedVisibility(
        visible = currentSong != null && musicControllerUiState.playerState != PlayerState.STOPPED,
        modifier = Modifier
    ) {
        if (currentSong != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                MusicCard(
                    modifier = Modifier,
                    cRbtSong = currentSong,
                    onPlayerEvent = crbtTonesViewModel::onEvent,
                    musicControllerUiState = musicControllerUiState,
                )
            }
        }
    }
}


fun LazyListScope.subscriptionTonesFeed(
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    onEvent: (TonesPlayerEvent) -> Unit,
    crbtSongs: List<CrbtSongResource>,
    currentSong: CrbtSongResource?,
    isPlaying: Boolean
) {

    items(
        items = crbtSongs,
        key = { it.id }
    ) { song ->
        val isCurrentSong = currentSong != null && currentSong == song
        SubscriptionTone(
            onSubscriptionToneClick = onSubscriptionToneClick,
            onGiftSubscriptionClick = onGiftSubscriptionClick,
            showDivider = crbtSongs.indexOf(song) != crbtSongs.size - 1,
            onPlayPauseToggle = {
                if (isCurrentSong) {
                    if (isPlaying) {
                        onEvent(TonesPlayerEvent.PauseSong)
                    } else {
                        onEvent(TonesPlayerEvent.ResumeSong)
                    }
                } else {
                    onEvent(TonesPlayerEvent.OnSongSelected(song))
                    onEvent(TonesPlayerEvent.PlaySong)
                }
            },
            isPlaying = isCurrentSong && isPlaying,
            crbtSongResource = song,
        )
    }
}

@Composable
fun SubscriptionTone(
    crbtSongResource: CrbtSongResource,
    modifier: Modifier = Modifier,
    onSubscriptionToneClick: (toneId: String) -> Unit,
    onGiftSubscriptionClick: (toneId: String) -> Unit,
    showDivider: Boolean = true,
    onPlayPauseToggle: () -> Unit,
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
            title = crbtSongResource.songTitle,
            subtitle = crbtSongResource.artisteName,
            trailingContent = {
                Row {
                    IconButton(
                        onClick = onPlayPauseToggle
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
            imageUrl = crbtSongResource.profile,
            colors = ListItemDefaults.colors(),
            modifier = Modifier
                .clickable(
                    onClick = { onSubscriptionToneClick(crbtSongResource.id) },
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
                        IconButton(onClick = { onGiftSubscriptionClick(crbtSongResource.id) }) {
                            Icon(imageVector = CrbtIcons.ArrowRight, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGiftSubscriptionClick(crbtSongResource.id) }
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
            onSubscriptionToneClick = {},
            onGiftSubscriptionClick = {},
            onPlayPauseToggle = {},
            isPlaying = false,
            crbtSongResource = DummyTones.tones[0]
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
            onGiftSubscriptionClick = {},
            musicControllerUiState = MusicControllerUiState(),
        )
    }
}