package com.crbt.subscription

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.MusicControllerUiState
import com.crbt.data.core.data.PlayerState
import com.crbt.data.core.data.TonesPlayerEvent
import com.crbt.data.core.data.repository.UserPackageResources
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.bodyFontFamily
import com.crbt.ui.core.ui.EmptyContent
import com.crbt.ui.core.ui.GiftPurchasePhoneNumber
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.blendMode
import com.crbt.ui.core.ui.musicPlayer.CrbtTonesViewModel
import com.crbt.ui.core.ui.musicPlayer.findCurrentMusicControllerSong
import com.itengs.crbt.core.model.data.PackageItem
import com.itengs.crbt.feature.subscription.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun CrbtSubscribeScreen(
    onSubscribeSuccess: () -> Unit,
    onBackClicked: () -> Unit,
    subscriptionViewModel: SubscriptionViewModel = hiltViewModel(),
    musicControllerUiState: MusicControllerUiState,
    crbtTonesViewModel: CrbtTonesViewModel,
) {

    val isGiftSub by subscriptionViewModel.isGiftSubscription.collectAsStateWithLifecycle()
    val registrationPackagesFlow by subscriptionViewModel.registrationPackagesFlow.collectAsStateWithLifecycle()
    val toneId by subscriptionViewModel.selectedTone.collectAsStateWithLifecycle()
    val crbtSong = crbtTonesViewModel.songByToneId(toneId ?: "")
    val subscriptionUiState = subscriptionViewModel.subscriptionUiState
    val isUserRegisteredForCrbt by subscriptionViewModel.isUserRegisteredForCrbt.collectAsStateWithLifecycle()
    val ussdState by subscriptionViewModel.ussdState.collectAsStateWithLifecycle()

    val tonesUiState by crbtTonesViewModel.uiState.collectAsStateWithLifecycle()
    val currentPlayingSong = tonesUiState.songs?.findCurrentMusicControllerSong(
        musicControllerUiState.currentSong?.tune ?: ""
    )

    val isCurrentlyPlayingSong = tonesUiState.songs?.findCurrentMusicControllerSong(
        crbtSong?.tune ?: ""
    ) == currentPlayingSong

    val isPlaying =
        isCurrentlyPlayingSong && musicControllerUiState.playerState == PlayerState.PLAYING


    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    when (crbtSong == null) {
        true -> Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        else ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    SubscribeHeader(
                        onBackClicked = onBackClicked,
                        artisteName = crbtSong.artisteName,
                        songTitle = crbtSong.songTitle,
                        songProfileUrl = crbtSong.profile,
                    )

                    MusicInfo(
                        price = crbtSong.price,
                        numberOfSubscribers = crbtSong.numberOfSubscribers,
                        numberOfPlays = crbtSong.numberOfListeners,
                        billingType = "/ ${crbtSong.subscriptionType.lowercase()}",
                        modifier = Modifier
                    )

                    SubscribeContent(
                        isGiftSubscription = isGiftSub ?: false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        onSubscribeClick = { giftPhoneNumber ->
                            subscriptionViewModel.subscribeToTone(
                                ussdCode = crbtSong.ussdCode,
                                activity = context as Activity,
                                onSuccess = {
                                    showBottomSheet = true
                                },
                                onError = { errorMessage ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = errorMessage,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                phoneNumber = giftPhoneNumber
                            )
                        },
                        subscriptionPrice = crbtSong.price.toDoubleOrNull() ?: 0.00,
                        isSubscriptionProcessing =
                        subscriptionUiState is SubscriptionUiState.Loading ||
                                ussdState is UssdUiState.Loading,
                        context = context,
                        isUserRegisteredForCrbt = isUserRegisteredForCrbt,
                        registerUserForCrbt = { packageId, code ->
                            subscriptionViewModel.runUssdCode(
                                ussdCode = "$code$packageId#",
                                onSuccess = {
                                    subscriptionViewModel.updateUserCrbtSubscriptionStatus(
                                        packageId = packageId
                                    )
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = it,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                activity = context as Activity
                            )
                        },
                        packagesResult = registrationPackagesFlow
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                ) {
                    val totalDuration = musicControllerUiState.totalDuration
                    val currentPosition = musicControllerUiState.currentPosition

                    val progress =
                        remember(totalDuration, currentPosition) {
                            if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f
                        }

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = -(15).dp, y = (32).dp)
                            .drawBehind {
                                val strokeWidthPx = 6.dp.toPx()
                                val radius = (size.minDimension - strokeWidthPx) / 2

                                drawCircle(
                                    color = Color.Transparent,
                                    radius = radius,
                                    style = Stroke(width = strokeWidthPx)
                                )

                                if (isCurrentlyPlayingSong) {
                                    drawArc(
                                        color = Color.White,
                                        startAngle = -90f,
                                        sweepAngle = 360f * progress,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidthPx),
                                        size = Size(
                                            size.width - strokeWidthPx,
                                            size.height - strokeWidthPx
                                        ),
                                        topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
                                    )
                                }
                            }
                    ) {
                        IconButton(
                            onClick = {
                                if (isCurrentlyPlayingSong) {
                                    if (isPlaying) {
                                        crbtTonesViewModel.onEvent(TonesPlayerEvent.PauseSong)
                                    } else {
                                        crbtTonesViewModel.onEvent(TonesPlayerEvent.ResumeSong)
                                    }
                                } else {
                                    crbtTonesViewModel.onEvent(
                                        TonesPlayerEvent.OnSongSelected(
                                            selectedSong = tonesUiState.songs?.find { it.id == crbtSong.id }!!
                                        )
                                    )
                                    crbtTonesViewModel.onEvent(TonesPlayerEvent.PlaySong)
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .matchParentSize()
                                .padding(2.dp)
                                .background(
                                    brush = Brush.linearGradient(CustomGradientColors),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isPlaying) CrbtIcons.Pause else CrbtIcons.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showBottomSheet) {
        SubscriptionSuccessBottomSheet(
            navigateUp = {
                showBottomSheet = false
                onSubscribeSuccess()
            },
            successMessage = stringResource(
                id = R.string.feature_subscription_success,
                crbtSong?.songTitle ?: "",
            )
        )
    }
}

@Composable
fun CrbtRegistrationContent(
    packageItems: List<PackageItem>,
    onSelectedPackage: (packageId: Int, ussdCode: String) -> Unit,
) {
    var selectedPackage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        packageItems.forEach { registrationPackage ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedPackage = registrationPackage.id
                        onSelectedPackage(
                            registrationPackage.id.toInt(),
                            registrationPackage.ussdCode
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedPackage == registrationPackage.id,
                    onClick = {
                        selectedPackage = registrationPackage.id
                        onSelectedPackage(
                            registrationPackage.id.toInt(),
                            registrationPackage.ussdCode
                        )
                    },
                )
                Text(
                    text = stringResource(
                        id = com.itengs.crbt.core.data.R.string.core_data_registration_package,
                        registrationPackage.price,
                        registrationPackage.itemValidity()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}


@Composable
fun SubscribeHeader(
    onBackClicked: () -> Unit,
    artisteName: String,
    songTitle: String,
    songProfileUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
    ) {
        DynamicAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = songProfileUrl,
            imageRes = com.itengs.crbt.core.ui.R.drawable.core_ui_paps_image
        )

        Column(
            modifier = Modifier
                .padding(top = 36.dp, start = 16.dp)
                .fillMaxSize()
                .align(Alignment.TopStart),
        ) {
            IconButton(
                onClick = onBackClicked,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = CrbtIcons.ArrowBack,
                    contentDescription = null,
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Text(
                text = songTitle,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.blendMode(BlendMode.Difference)
            )
            Text(
                text = artisteName,
                color = Color.White,
                modifier = Modifier.blendMode(BlendMode.Difference)
            )
        }
    }
}


@Composable
fun MusicInfo(
    price: String,
    numberOfSubscribers: Int,
    numberOfPlays: Int,
    billingType: String,
    modifier: Modifier
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            InfoButton(
                value = (price.toDoubleOrNull() ?: 0.00).toString(),
                title = stringResource(id = R.string.feature_subscription_price, billingType),
                buttonColors = ButtonDefaults.buttonColors(),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        item {
            InfoButton(
                value = numberOfSubscribers.toString(),
                title = pluralStringResource(
                    id = R.plurals.feature_subscription_subscribers_plural,
                    count = numberOfSubscribers,
                ),
                buttonColors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        item {
            InfoButton(
                value = numberOfPlays.toString(),
                title = pluralStringResource(
                    id = R.plurals.feature_subscription_plays_plural,
                    count = numberOfPlays,
                ),
                buttonColors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}


@Composable
fun InfoButton(
    value: String,
    title: String,
    buttonColors: ButtonColors,
    modifier: Modifier = Modifier
) {
    Button(onClick = { }, colors = buttonColors) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = bodyFontFamily
                )
            )
            Text(text = title, style = MaterialTheme.typography.labelMedium)
        }
    }

}

@Composable
fun SubscribeContent(
    modifier: Modifier = Modifier,
    isGiftSubscription: Boolean,
    onSubscribeClick: (phoneNumber: String) -> Unit,
    subscriptionPrice: Double,
    isSubscriptionProcessing: Boolean,
    context: Context,
    isUserRegisteredForCrbt: Boolean,
    registerUserForCrbt: (packageId: Int, ussdCode: String) -> Unit,
    packagesResult: Result<UserPackageResources?>,
) {
    val title = when (isGiftSubscription) {
        true ->
            stringResource(
                id = R.string.feature_subscription_gift_song_title, ""
            ) to stringResource(
                id = R.string.feature_subscription_gift_song_subtitle
            )

        else ->
            if (isUserRegisteredForCrbt) {
                stringResource(
                    id = R.string.feature_subscription_subscribe_to_song_title,
                    ""
                ) to stringResource(
                    id = R.string.feature_subscription_subscribe_to_song_subtitle
                )
            } else {
                stringResource(
                    id = R.string.feature_subscription_register_for_crbt
                ) to
                        stringResource(
                            id = R.string.feature_subscription_register_for_crbt_description
                        )
            }
    }

    var giftPhoneNumber by remember {
        mutableStateOf(
            "" to false
        )
    }

    var packageId by remember {
        mutableStateOf(0 to "")
    }

    OnboardingSheetContainer(
        title = title.first,
        subtitle = title.second,
        content = {
            if (isGiftSubscription) {
                Spacer(modifier = Modifier.height(8.dp))
                GiftPurchasePhoneNumber(
                    onPhoneNumberChanged = { phoneNumber, isValid ->
                        giftPhoneNumber = phoneNumber to isValid
                    },
                    modifier = Modifier.fillMaxWidth(),
                    context = context
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (!isUserRegisteredForCrbt && !isGiftSubscription) {
                when (packagesResult) {
                    is Result.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Result.Error -> {
                        EmptyContent(
                            description = packagesResult.exception.message ?: "An error occurred"
                        )
                    }

                    is Result.Success -> {
                        val packageItems = packagesResult.data?.packageItems ?: emptyList()
                        if (packageItems.isNotEmpty()) {
                            CrbtRegistrationContent(
                                packageItems = packageItems,
                                onSelectedPackage = { id, ussdCode ->
                                    packageId = id to ussdCode
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }


            ProcessButton(
                onClick = {
                    if (!isUserRegisteredForCrbt && !isGiftSubscription) {
                        registerUserForCrbt(packageId.first, packageId.second)
                    } else {
                        onSubscribeClick(giftPhoneNumber.first)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            CustomGradientColors
                        ),
                        shape = ButtonDefaults.shape
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                textContent = {

                    val subscribeTextAmountPair = when (isGiftSubscription) {
                        true ->
                            stringResource(id = R.string.feature_subscription_gift_button) to subscriptionPrice.toString()

                        else -> {
                            val text =
                                if (!isUserRegisteredForCrbt) {
                                    stringResource(id = R.string.feature_subscription_register) to
                                            (packagesResult as? Result.Success)?.data?.packageItems?.find { it.id == packageId.first.toString() }?.price.takeIf {
                                                !it.isNullOrEmpty()
                                            }

                                } else {
                                    stringResource(R.string.feature_subscription_subscribe_button) to subscriptionPrice.toString()
                                }
                            text
                        }
                    }

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            ) {
                                append(subscribeTextAmountPair.first)
                                append(" ")
                                append(
                                    stringResource(
                                        id = R.string.feature_subscription_etb,
                                        subscribeTextAmountPair.second ?: ""
                                    )
                                )
                            }
                        }
                    )
                },
                isEnabled = when (isGiftSubscription) {
                    true -> giftPhoneNumber.second
                    else ->
                        if (!isUserRegisteredForCrbt)
                            packageId.second.isNotEmpty() && packageId.first > 0
                        else true
                },
                isProcessing = isSubscriptionProcessing
            )
        },
        modifier = modifier
    )
}

@Composable
fun AnimatedSuccessCheckmark(
    modifier: Modifier = Modifier,
    circleColor: Color = Color.Green,
    checkmarkColor: Color = Color.White,
    durationMillis: Int = 1000,
    strokeWidth: Float = 8f
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing)
        )
    }

    Canvas(modifier = modifier.size(120.dp)) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2

        drawArc(
            color = circleColor,
            startAngle = -90f,
            sweepAngle = 360f * progress.value,
            useCenter = false,
            style = Stroke(width = strokeWidth),
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(width = radius * 2, height = radius * 2)
        )

        val checkmarkPath = Path().apply {
            moveTo(center.x - radius * 0.4f, center.y)
            lineTo(center.x - radius * 0.1f, center.y + radius * 0.3f)
            lineTo(center.x + radius * 0.4f, center.y - radius * 0.3f)
        }

        if (progress.value > 0.5f) {
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(checkmarkPath, false)

            val animatedPath = Path()
            pathMeasure.getSegment(
                0f,
                pathMeasure.length * ((progress.value - 0.5f) * 2),
                animatedPath,
                true
            )

            drawPath(
                path = animatedPath,
                color = checkmarkColor,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionSuccessBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    navigateUp: () -> Unit,
    successMessage: String,
) {
    ModalBottomSheet(
        onDismissRequest = navigateUp,
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedSuccessCheckmark(
                    modifier = Modifier.padding(16.dp),
                    circleColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.primary,
                    durationMillis = 1200
                )

                Text(
                    text = successMessage,
                    style = MaterialTheme.typography.bodyMedium
                )

                ProcessButton(
                    onClick = navigateUp,
                    text = stringResource(id = R.string.feature_subscription_done),
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    )
}


@Preview
@Composable
fun AnimatedSuccessCheckmarkPreview() {
    AnimatedSuccessCheckmark(
        modifier = Modifier.padding(16.dp),
        circleColor = Color.Green,
        checkmarkColor = Color.Green,
        durationMillis = 1200
    )
}


@ThemePreviews
@Composable
fun CrbtSubscribeHeaderPreview() {
    val song = DummyTones.tones[0]
    CrbtTheme {
        SubscribeHeader(
            onBackClicked = {},
            artisteName = song.artisteName,
            songTitle = song.songTitle,
            songProfileUrl = song.profile,
        )
    }
}

@ThemePreviews
@Composable
fun CrbtSubscribeContentPreview() {
    CrbtTheme {
        SubscribeContent(
            isGiftSubscription = false,
            onSubscribeClick = {},
            subscriptionPrice = 10.30,
            isSubscriptionProcessing = false,
            context = LocalContext.current,
            isUserRegisteredForCrbt = false,
            registerUserForCrbt = { _, _ -> },
            packagesResult = Result.Loading
        )
    }
}

@ThemePreviews
@Composable
fun CrbtSubscribeContentPreview2() {
    CrbtTheme {
        SubscribeContent(
            isGiftSubscription = true,
            onSubscribeClick = {},
            subscriptionPrice = 10.30,
            isSubscriptionProcessing = false,
            context = LocalContext.current,
            isUserRegisteredForCrbt = false,
            registerUserForCrbt = { _, _ -> },
            packagesResult = Result.Loading
        )
    }
}