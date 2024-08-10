package com.crbt.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.example.crbtjetcompose.feature.home.R


enum class PopularTodayOptions {
    Tones,
    Albums
}

// tab layout for popular today
@Composable
fun PopularTodayTabLayout(
    modifier: Modifier,
    navigateToSubscriptions: (toneId: String?) -> Unit
) = trace("PopularTodayTabLayout") {

    var selectedTabIndex by remember { mutableIntStateOf(0) }


    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.feature_home_popular_today_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        TabRow(
            selectedTabIndex = selectedTabIndex,
            divider = {},
            containerColor = Color.Transparent,
            indicator = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PopularTodayOptions.entries.forEachIndexed { index, option ->
                    Text(
                        text = option.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .wrapContentSize()
                            .background(
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                            .border(
                                width = 1.dp,
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                            .clickable(
                                onClick = {
                                    selectedTabIndex = index
                                },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        when (PopularTodayOptions.entries[selectedTabIndex]) {
            PopularTodayOptions.Tones -> {
                DailyPopularTones(
                    popularTonesUiState = PopularTonesUiState.Shown(
                        tones = DummyTones.tones
                    ),
                    onToneSelected = { toneId ->
                        navigateToSubscriptions(toneId)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            PopularTodayOptions.Albums -> {
                DailyPopularTones(
                    popularTonesUiState = PopularTonesUiState.Shown(
                        tones = DummyTones.tones.reversed()
                    ),
                    onToneSelected = { toneId ->
                        navigateToSubscriptions(toneId)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun DailyPopularTones(
    popularTonesUiState: PopularTonesUiState.Shown,
    onToneSelected: (String) -> Unit,
    modifier: Modifier
) = trace("DailyPopularTones") {
    val lazyGridState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        LazyRow(
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
            modifier = Modifier
                .heightIn(max = max(160.dp, with(LocalDensity.current) { 160.sp.toDp() }))
                .fillMaxWidth()
        ) {
            items(
                items = popularTonesUiState.tones,
                key = { tone -> tone.id }
            ) { tone ->
                MusicCard(
                    artist = tone.artist,
                    title = tone.toneName,
                    imageUrl = tone.toneImageUrl,
                    onCardClick = { onToneSelected(tone.id) }
                )
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
                imageRes = com.example.crbtjetcompose.core.designsystem.R.drawable.core_designsystem_ic_placeholder_default
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
        popularTonesUiState = PopularTonesUiState.Shown(
            tones = DummyTones.tones
        ),
        onToneSelected = {},
        modifier = Modifier.fillMaxWidth()
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
        navigateToSubscriptions = {}
    )
}

