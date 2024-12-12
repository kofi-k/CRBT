package com.crbt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import com.crbt.data.core.data.repository.CrbtSongsFeedUiState
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.crbt.ui.core.ui.EmptyContent
import com.example.crbtjetcompose.feature.home.R


@Composable
fun PopularTodayTabLayout(
    modifier: Modifier,
    navigateToSubscriptions: (toneId: String) -> Unit,
    crbSongsFeed: CrbtSongsFeedUiState,
) = trace("PopularTodayTabLayout") {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedCrbtCategory by remember { mutableStateOf("") }

    LaunchedEffect(crbSongsFeed) {
        if (crbSongsFeed is CrbtSongsFeedUiState.Success) {
            selectedCrbtCategory = crbSongsFeed.songs.first().category
        }
    }

    Column(modifier = modifier) {
        when (crbSongsFeed) {
            is CrbtSongsFeedUiState.Success -> {
                val listOfFeedCategories = crbSongsFeed.songs.map { it.category }.distinct()
                Text(
                    text = stringResource(id = R.string.feature_home_popular_today_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {},
                    containerColor = Color.Transparent,
                    indicator = {},
                    edgePadding = 16.dp,
                    tabs = {
                        listOfFeedCategories.forEachIndexed { index, category ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = {
                                    selectedTabIndex = index
                                    selectedCrbtCategory = category
                                },
                                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(
                                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            ) {
                                Text(
                                    text = category,
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        )
                                )
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            else -> Unit
        }

        DailyPopularTones(
            crbtSongsFeedUiState = crbSongsFeed,
            onToneSelected = navigateToSubscriptions,
            selectedCrbtCategory = selectedCrbtCategory
        )

    }
}


@Composable
fun DailyPopularTones(
    crbtSongsFeedUiState: CrbtSongsFeedUiState,
    onToneSelected: (String) -> Unit,
    selectedCrbtCategory: String = ""
) = trace("DailyPopularTones") {
    val lazyGridState = rememberLazyListState()

    when (crbtSongsFeedUiState) {
        is CrbtSongsFeedUiState.Error, CrbtSongsFeedUiState.Loading -> Unit
        is CrbtSongsFeedUiState.Success -> {
            when (crbtSongsFeedUiState.songs.isEmpty()) {
                true -> {
                    SurfaceCard(
                        content = {
                            EmptyContent(
                                description = stringResource(id = R.string.feature_home_no_songs),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                false -> {
                    LazyRow(
                        state = lazyGridState,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 6.dp
                        ),
                        modifier = Modifier
                            .heightIn(
                                max = max(
                                    160.dp,
                                    with(LocalDensity.current) { 160.sp.toDp() })
                            )
                            .fillMaxWidth()
                    ) {
                        items(
                            items = crbtSongsFeedUiState
                                .songs
                                .filter { it.category == selectedCrbtCategory },
                            key = { tone -> tone.id }
                        ) { tone ->
                            MusicCard(
                                artist = tone.artisteName,
                                title = tone.songTitle,
                                imageUrl = tone.profile,
                                onCardClick = { onToneSelected(tone.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MusicCard(
    artist: String,
    title: String,
    imageUrl: String,
    onCardClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .widthIn(max = 120.dp)
            .clickable(
                onClick = onCardClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .clip(MaterialTheme.shapes.large),
        ) {
            DynamicAsyncImage(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
                imageRes = com.example.crbtjetcompose.core.ui.R.drawable.core_ui_paps_image
            )
        }
        Spacer(modifier = Modifier.size(2.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    slightlyDeemphasizedAlpha
                )
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDailyPopularTones() {
    DailyPopularTones(
        crbtSongsFeedUiState = CrbtSongsFeedUiState.Loading,
        onToneSelected = {},
    )
}

@Preview
@Composable
fun PreviewMusicCard() {
    MusicCard(
        artist = "Artist",
        title = "Title",
        imageUrl = "https://picsum.photos/200/300",
        onCardClick = {}
    )
}

@Preview
@Composable
fun PreviewPopularTodayTabLayout() {
    PopularTodayTabLayout(
        modifier = Modifier.fillMaxWidth(),
        navigateToSubscriptions = {},
        crbSongsFeed = CrbtSongsFeedUiState.Success(songs = emptyList()),
    )
}

