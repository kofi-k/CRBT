package com.crbt.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.scrollbar.DecorativeScrollbar
import com.crbt.designsystem.components.scrollbar.scrollbarState
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha


enum class PopularTodayOptions{
    Tones,
    Albums
}

// tab layout for popular today
@Composable
fun PopularTodayTabLayout(
    selectedOption: PopularTodayOptions,
    onOptionSelected: (PopularTodayOptions) -> Unit,
    modifier: Modifier
) = trace("PopularTodayTabLayout") {



}




@Composable
fun DailyPopularTones(
    popularTonesUiState: PopularTonesUiState.Shown,
    onToneSelected: (String) -> Unit,
    modifier: Modifier
) = trace("DailyPopularTones") {
    val lazyGridState = rememberLazyListState()

    Box(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier
                .heightIn(max = max(140.dp, with(LocalDensity.current) { 140.sp.toDp() }))
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

        lazyGridState.DecorativeScrollbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .align(Alignment.BottomStart),
            state = lazyGridState.scrollbarState(itemsAvailable = popularTonesUiState.tones.size),
            orientation = Orientation.Horizontal,
        )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onCardClick)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                ),
        ) {
            DynamicAsyncImage(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    slightlyDeemphasizedAlpha
                )
            )
        )
    }
}

