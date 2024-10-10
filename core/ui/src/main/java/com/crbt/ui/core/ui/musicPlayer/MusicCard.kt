package com.crbt.ui.core.ui.musicPlayer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.DummyTones
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme


@Composable
fun MusicCard(
    onPlay: () -> Unit,
    onPaused: () -> Unit,
    onSkipPreviousClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    musicTitle: String,
    musicArtist: String,
    isPlaying: Boolean,
    musicCoverUrl: String,
    modifier: Modifier = Modifier
) {

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
                    musicTitle = musicTitle,
                    musicArtist = musicArtist,
                    coverUrl = musicCoverUrl,
                    modifier = Modifier.weight(1f)
                )

                MusicControls(
                    onPlay = onPlay,
                    onPaused = onPaused,
                    onSkipPreviousClick = onSkipPreviousClick,
                    onSkipNextClick = onSkipNextClick,
                    isPlaying = isPlaying
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProgressIndicatorBorder(progress: Float, gradientColors: List<Color>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(IntrinsicSize.Min)  // Intrinsic size for dynamic height
    ) {
        val strokeWidth = 6.dp.toPx()
        val cardWidth = size.width
        val cardHeight = size.height

        // Calculate the total perimeter of the card (border length)
        val totalLength = (cardWidth * 2) + (cardHeight * 2)

        // Calculate the progress length
        val progressLength = totalLength * progress

        // Create a linear gradient brush
        val gradientBrush = Brush.linearGradient(
            colors = gradientColors,
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height)
        )

        // Draw the border based on the progress
        val path = Path().apply {
            moveTo(0f, 0f)
            var drawnLength = 0f

            // Top edge
            val topEdgeLength = cardWidth
            if (drawnLength + topEdgeLength <= progressLength) {
                lineTo(cardWidth, 0f)
                drawnLength += topEdgeLength
            } else {
                lineTo(drawnLength + (progressLength - drawnLength), 0f)
                return@apply
            }

            // Right edge
            val rightEdgeLength = cardHeight
            if (drawnLength + rightEdgeLength <= progressLength) {
                lineTo(cardWidth, cardHeight)
                drawnLength += rightEdgeLength
            } else {
                lineTo(cardWidth, drawnLength + (progressLength - drawnLength))
                return@apply
            }

            // Bottom edge
            val bottomEdgeLength = cardWidth
            if (drawnLength + bottomEdgeLength <= progressLength) {
                lineTo(0f, cardHeight)
                drawnLength += bottomEdgeLength
            } else {
                lineTo(cardWidth - (progressLength - drawnLength), cardHeight)
                return@apply
            }

            // Left edge
            val leftEdgeLength = cardHeight
            if (drawnLength + leftEdgeLength <= progressLength) {
                lineTo(0f, 0f)
                drawnLength += leftEdgeLength
            } else {
                lineTo(0f, cardHeight - (progressLength - drawnLength))
            }
        }

        // Draw the path with the gradient brush
        drawPath(
            path = path,
            brush = gradientBrush,  // Use the gradient brush instead of a solid color
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
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
    onPlay: () -> Unit,
    onPaused: () -> Unit,
    onSkipPreviousClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onSkipPreviousClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(imageVector = CrbtIcons.Previous, contentDescription = null)
        }
        IconButton(
            onClick = if (isPlaying) onPaused else onPlay,
            modifier = Modifier
                .size(48.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors()
        ) {
            Icon(
                imageVector = if (isPlaying) CrbtIcons.Pause else CrbtIcons.PlayArrow,
                contentDescription = null
            )
        }
        IconButton(
            onClick = onSkipNextClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(imageVector = CrbtIcons.Next, contentDescription = null)
        }

    }
}


@ThemePreviews
@Composable
fun MusicCardPreview() {
    CrbtTheme {
        MusicCard(
            onPlay = {},
            onSkipPreviousClick = {},
            onSkipNextClick = {},
            musicTitle = "Music Title",
            musicArtist = "Music Artist",
            isPlaying = false,
            musicCoverUrl = DummyTones.tones[0].profile,
            onPaused = {}
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