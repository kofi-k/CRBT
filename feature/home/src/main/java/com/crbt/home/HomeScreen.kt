package com.crbt.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.CrbtUssdType
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.model.DummyUser
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.CHECK_BALANCE_USSD
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.UssdResponseDialog
import com.example.crbtjetcompose.core.model.data.mapToUserToneSubscriptions
import com.example.crbtjetcompose.feature.home.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onSubscriptionClick: (String?) -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    onPopularTodayClick: (String?) -> Unit = {}
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val ussdUiState by viewModel.ussdState.collectAsStateWithLifecycle()
    val userDataUiState by viewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val latestMusicUiState by viewModel.latestCrbtSong.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    var showDialog by remember {
        mutableStateOf(false)
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
        contentPadding = PaddingValues(
            vertical = 24.dp
        )
    ) {
        item {
            UserBalanceCard(
                onNavigateToTopUp = onNavigateToTopUp,
                onRefresh = {
                    viewModel.runUssdCode(
                        ussdCode = CHECK_BALANCE_USSD,
                        onSuccess = {
                            showDialog = true
                        },
                        onError = {
                            showDialog = true
                        }
                    )
                },
                isRefreshing = ussdUiState is UssdUiState.Loading,
                userPreferenceUiState = userDataUiState
            )
        }

        item {
            CrbtAds()
        }

        item {
            when (latestMusicUiState) {
                is Result.Error -> Unit
                Result.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Result.Success -> {
                    val latestMusic = (latestMusicUiState as Result.Success).data
                    LatestMusicCard(
                        artist = latestMusic.artisteName,
                        title = latestMusic.songTitle,
                        backgroundUrl = latestMusic.profile,
                        onCardClick = {
                            onPopularTodayClick(null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }

        item {
            PopularTodayTabLayout(
                navigateToSubscriptions = onPopularTodayClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            RecentSubscription(
                onSubscriptionClick = onSubscriptionClick,
                userSubscriptions = DummyTones.tones.mapToUserToneSubscriptions(DummyUser.user),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }

    if (showDialog) {
        UssdResponseDialog(
            onDismiss = {
                showDialog = false
            },
            ussdUiState = ussdUiState,
            crbtUssdType = CrbtUssdType.BALANCE_CHECK
        )
    }
}

@Composable
internal fun UserBalanceCard(
    onNavigateToTopUp: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean = false,
    userPreferenceUiState: UserPreferenceUiState
) {
    Card(
        onClick = onNavigateToTopUp,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .drawWithCache {
                val brush = Brush.linearGradient(CustomGradientColors)
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                when (userPreferenceUiState) {
                    is UserPreferenceUiState.Success ->
                        Text(
                            text = stringResource(
                                id = R.string.feature_home_balance,
                                userPreferenceUiState.userData.currentBalance
                            ),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.White
                        )

                    else -> CircularProgressIndicator()
                }

                Text(
                    text = stringResource(id = R.string.feature_home_balance_subtitle),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(
                onClick = onRefresh
            ) {
                when (isRefreshing) {
                    true -> CircularProgressIndicator(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        color = Color.White
                    )

                    else ->
                        Icon(
                            imageVector = CrbtIcons.Refresh,
                            contentDescription = CrbtIcons.Refresh.name,
                            modifier = Modifier,
                            tint = Color.White
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
    backgroundUrl: String?,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
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

@RequiresApi(Build.VERSION_CODES.O)
@ThemePreviews
@Composable
fun PreviewHomeScreen() {
    CrbtTheme {
        HomeScreen()
    }
}