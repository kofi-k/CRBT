package com.crbt.home

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.repository.CrbtAdsUiState
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.ui.core.ui.launchCustomChromeTab
import com.itengs.crbt.feature.home.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrbtAds(
    crbtAdsUiState: CrbtAdsUiState
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

    when (crbtAdsUiState) {
        is CrbtAdsUiState.Success -> {
            val crbtAds = crbtAdsUiState.data

            if (crbtAds.isNotEmpty()) {
                HorizontalUncontainedCarousel(
                    state = rememberCarouselState { crbtAds.count() },
                    modifier = Modifier
                        .width(412.dp)
                        .height(221.dp),
                    itemWidth = 186.dp,
                    itemSpacing = 8.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) { index ->
                    val ad = crbtAds[index]
                    AdvertCard(
                        imageUrl = ad.image,
                        title = ad.description,
                        modifier = Modifier
                            .height(205.dp)
                            .maskClip(MaterialTheme.shapes.extraLarge)
                            .clickable {
                                if (ad.url.isNotBlank()) {
                                    launchCustomChromeTab(
                                        context = context,
                                        uri = Uri.parse(ad.url),
                                        toolbarColor = backgroundColor
                                    )
                                }
                            },
                    )
                }
            }
        }

//        is CrbtAdsUiState.Loading -> Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            CircularProgressIndicator()
//        }

        else -> Unit
    }

}

@Composable
fun AdvertCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
) {
    Box(
        modifier = modifier
    ) {
        DynamicAsyncImage(
            modifier = Modifier.fillMaxSize(),
            base64ImageString = imageUrl
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.feature_home_advert),
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            Brush.linearGradient(CustomGradientColors),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .padding(horizontal = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = size.height / 2,
                            endY = size.height
                        )
                    )
                }
        )
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 5.dp)
                .padding(horizontal = 12.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Preview
@Composable
fun AdvertCardPreview() {
    AdvertCard(
        imageUrl = "https://picsum.photos/200/300",
        title = "Advert Title"
    )
}