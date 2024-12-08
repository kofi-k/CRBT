package com.crbt.ui.core.ui.musicPlayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.MusicControllerUiState
import com.crbt.data.core.data.PlayerState
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.bodyFontFamily
import com.example.crbtjetcompose.core.model.data.CrbtSongResource
import com.example.crbtjetcompose.core.ui.R


@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    cRbtSong: CrbtSongResource,
    musicControllerUiState: MusicControllerUiState,
    onPlayerEvent: (TonesPlayerEvent) -> Unit
) {

    val isPlaying = musicControllerUiState.playerState == PlayerState.PLAYING

    SurfaceCard(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .then(modifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MusicInfo(
                    musicTitle = cRbtSong.songTitle,
                    musicArtist = cRbtSong.artisteName,
                    coverUrl = cRbtSong.profile,
                    modifier = Modifier.weight(1f)
                )

                MusicControls(
                    onPausePlayToggle = {
                        onPlayerEvent(
                            if (isPlaying) TonesPlayerEvent.PauseSong else TonesPlayerEvent.ResumeSong
                        )
                    },
                    isPlaying = isPlaying,
                    price = cRbtSong.price,
                    numberOfSubscribers = cRbtSong.numberOfSubscribers
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun MusicInfo(
    musicTitle: String,
    musicArtist: String,
    coverUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.large)
        ) {
            DynamicAsyncImage(
                imageUrl = coverUrl,
                modifier = Modifier.fillMaxSize(),
                imageRes = R.drawable.core_ui_paps_image,
            )
        }
        Column {
            Text(
                text = musicTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = musicArtist,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MusicControls(
    onPausePlayToggle: () -> Unit,
    isPlaying: Boolean,
    price: String,
    numberOfSubscribers: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.End
        ) {
            SurfaceCard(content = {
                Text(
                    text = "$price ETB",
                    style = MaterialTheme.typography.bodySmall.copy(
                        brush = Brush.linearGradient(CustomGradientColors),
                        fontFamily = bodyFontFamily
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(6.dp, 2.dp)
                )
            })

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = CrbtIcons.People,
                    contentDescription = CrbtIcons.People.name,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pluralStringResource(
                        id = R.plurals.core_ui_music_player_subscribers,
                        count = numberOfSubscribers,
                        numberOfSubscribers
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        IconButton(
            onClick = onPausePlayToggle,
            colors = IconButtonDefaults.iconButtonColors()
        ) {
            Icon(
                imageVector = if (isPlaying) CrbtIcons.Pause else CrbtIcons.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}


@ThemePreviews
@Composable
fun MusicCardPreview() {
    CrbtTheme {
        MusicCard(
            musicControllerUiState = MusicControllerUiState(),
            onPlayerEvent = {},
            cRbtSong = DummyTones.tones[0],
        )
    }
}


@ThemePreviews
@Composable
fun MusicInfoPreview() {
    CrbtTheme {
        MusicInfo(
            musicTitle = "Music Title",
            musicArtist = "Music Artist",
            coverUrl = DummyTones.tones[0].profile
        )
    }
}