package com.crbt.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtGradientBackground
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.LocalGradientColors
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.example.crbtjetcompose.feature.home.R


@Composable
fun HomeScreen(){
    Column {
        UserBalanceCard(
            onNavigateToTopUp = {},
            onRefresh = {},
            balance = "1000",
            balancePercentage = 65
        )

        Spacer(modifier = Modifier.size(16.dp))

        LatestMusicCard(
            artist = "Artist",
            title = "Title",
            backgroundUrl = "",
            onCardClick = {}
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
    CrbtGradientBackground(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        gradientColors = LocalGradientColors.current
    ) {
        Card(
            onClick = onNavigateToTopUp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PercentIndicator(
                    percentage = balancePercentage,
                    modifier = Modifier.size(70.dp)
                )
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
}

@Composable
fun LatestMusicCard(
    artist: String,
    title: String,
    backgroundUrl: String,
    onCardClick: () -> Unit
) {
    OutlinedCard(
        onClick = onCardClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
    ) {
        Box {
            DynamicAsyncImage(
                imageUrl = backgroundUrl,
                modifier = Modifier.fillMaxSize(),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = artist,
                    modifier = Modifier.padding(top = (-8).dp),
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
    backgroundColor: Color = MaterialTheme.colorScheme.outline
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
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )
    }
}


@Preview
@Composable
fun PreviewUserBalanceCard() {
    CrbtTheme {
        UserBalanceCard(
            onNavigateToTopUp = {},
            onRefresh = {},
            balance = "1000",
            balancePercentage = 65
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

@Preview
@Composable
fun PreviewHomeScreen() {
    CrbtTheme {
        HomeScreen()
    }
}