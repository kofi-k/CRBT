package com.crbt.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.model.DummyUser
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.example.crbtjetcompose.core.model.data.mapToUserToneSubscriptions
import com.example.crbtjetcompose.feature.home.R


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UserBalanceCard(
            onNavigateToTopUp = {},
            onRefresh = {},
            balance = "85.40",
            balancePercentage = 65
        )

        LatestMusicCard(
            artist = DummyTones.tones[0].artist,
            title = DummyTones.tones[0].toneName,
            backgroundUrl = DummyTones.tones[0].toneImageUrl,
            onCardClick = {}
        )

        // todo put popular tones here

        RecentSubscription(
            onSubscriptionClick = {},
            userSubscriptions = DummyTones.tones.mapToUserToneSubscriptions( DummyUser.user)
        )
    }
}

@Composable
internal fun UserBalanceCard(
    onNavigateToTopUp: () -> Unit,
    onRefresh: () -> Unit,
    balance: String,
    balancePercentage: Int,
    isRefreshing: Boolean = false
) {
    Card(
        onClick = onNavigateToTopUp,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .drawWithCache {
                val brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF9E82F0),
                        Color(0xFF42A5F5)
                    )
                )
                onDrawBehind {
                    drawRoundRect(
                        brush,
                        cornerRadius = CornerRadius(24.dp.toPx())
                    )
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
            ) {
                PercentIndicator(
                    percentage = balancePercentage,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_home_balance, balance),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black
                    ),
                )
                Text(
                    text = stringResource(id = R.string.feature_home_balance_subtitle),
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(
                onClick = onRefresh
            ) {

                val rotateIcon by animateFloatAsState(
                    targetValue = if (isRefreshing) 360f else 0f,
                    label = "rotateIcon"
                )

                Icon(
                    imageVector = CrbtIcons.Refresh,
                    contentDescription = CrbtIcons.Refresh.name,
                    modifier = Modifier.graphicsLayer(
                        rotationZ = rotateIcon
                    ),
                )
            }
        }
    }
}

@Composable
fun LatestMusicCard(
    artist: String,
    title: String,
    backgroundUrl: String?,
    onCardClick: () -> Unit
) {
    Box {
        DynamicAsyncImage(
            imageUrl = backgroundUrl,
            imageRes = R.drawable.paps_image,
            modifier = Modifier
                .matchParentSize()
                .clip(MaterialTheme.shapes.large)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.large
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        )
        OutlinedCard(
            onClick = onCardClick,
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.feature_home_latest_music),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Text(
                    text = artist,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun PercentIndicator(
    modifier: Modifier = Modifier,
    percentage: Int,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.outline.copy(stronglyDeemphasizedAlpha)
) {

    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.toFloat(),
        label = "animatedPercentage"
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .drawBehind {
                // Draw the background arc (gray)
                drawArc(
                    color = backgroundColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size.copy(width = size.minDimension, height = size.minDimension),
                    style = Stroke(width = 40f)
                )
                // Draw the percentage arc (colored)
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedPercentage / 100,
                    useCenter = false,
                    size = size.copy(width = size.minDimension, height = size.minDimension),
                    style = Stroke(width = 40f)
                )
            }
            .size(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            ),
        )
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
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    CrbtTheme {
        HomeScreen()
    }
}