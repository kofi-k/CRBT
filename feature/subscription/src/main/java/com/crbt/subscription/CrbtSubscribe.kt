package com.crbt.subscription

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.SubscriptionBillingType
import com.crbt.data.core.data.util.simpleDateFormatPattern
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.CustomInputButton
import com.crbt.ui.core.ui.GiftPurchasePhoneNumber
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.ShowDatePicker
import com.example.crbtjetcompose.feature.subscription.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun CrbtSubscribeScreen(
    onSubscribeClick: () -> Unit,
    onBackClicked: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel(),
) {

    val isGiftSub by viewModel.isGiftSubscription.collectAsStateWithLifecycle()
    val crbtSong by viewModel.crbtSongResource.collectAsStateWithLifecycle()
    val subscriptionUiState by viewModel.subscriptionUiState.collectAsStateWithLifecycle()
    val isUserOnCrbtSubscription by viewModel.isUserOnCrbtSubscription.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    LaunchedEffect(subscriptionUiState) {
        when (subscriptionUiState) {
            is SubscriptionUiState.Success -> onSubscribeClick()

            is SubscriptionUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (subscriptionUiState as SubscriptionUiState.Error).error,
                    duration = SnackbarDuration.Short
                )
            }

            else -> Unit
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SubscribeHeader(
            onBackClicked = onBackClicked,
            artisteName = crbtSong?.artisteName ?: "",
            songTitle = crbtSong?.songTitle ?: "",
            songProfileUrl = crbtSong?.profile ?: ""
        )
        SubscribeContent(
            isGiftSubscription = isGiftSub ?: false,
            onBillingTypeSelected = {},
            billingType = SubscriptionBillingType.Monthly,
            onDatePicked = {},
            date = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            onSubscribeClick = {
                if (isUserOnCrbtSubscription == true) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "You are already on a CRBT subscription",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    viewModel.subscribeToTone(
                        ussdCode = crbtSong?.ussdCode ?: "",
                        activity = context as Activity
                    )
                }
            },
            subscriptionPrice = crbtSong?.price?.toDoubleOrNull() ?: 0.00,
            isSubscriptionProcessing = subscriptionUiState == SubscriptionUiState.Loading,
            isButtonEnabled = crbtSong != null
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            .height(260.dp)
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
            imageRes = R.drawable.feature_subscription_onboardingbackground
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
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artisteName,
                color = Color.White
            )
        }
    }
}

@Composable
fun SubscribeContent(
    modifier: Modifier = Modifier,
    isGiftSubscription: Boolean,
    onGiftPhoneNumberChanged: (String, Boolean) -> Unit = { _, _ -> },
    onBillingTypeSelected: (SubscriptionBillingType) -> Unit,
    billingType: SubscriptionBillingType,
    onDatePicked: (Long) -> Unit,
    date: Long?,
    onSubscribeClick: () -> Unit,
    subscriptionPrice: Double,
    isSubscriptionProcessing: Boolean,
    isButtonEnabled: Boolean
) {
    val title = if (isGiftSubscription) {
        stringResource(id = R.string.feature_subscription_gift_song_title, "")
    } else {
        stringResource(id = R.string.feature_subscription_subscribe_to_song_title, "")
    }
    OnboardingSheetContainer(
        title = title,
        subtitle = stringResource(id = R.string.feature_subscription_subscribe_to_song_subtitle),
        content = {
            if (isGiftSubscription) {
                Spacer(modifier = Modifier.height(8.dp))
                GiftPurchasePhoneNumber(
                    onPhoneNumberChanged = onGiftPhoneNumberChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            BillingType(
                onBillingTypeSelected = onBillingTypeSelected,
                billingType = billingType
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubscriptionDate(
                onDatePicked = onDatePicked,
                date = date
            )
            Spacer(modifier = Modifier.height(16.dp))
            ProcessButton(
                onClick = onSubscribeClick,
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
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ).toSpanStyle()
                            ) {
                                val text = if (isGiftSubscription) {
                                    stringResource(id = R.string.feature_subscription_gift_button)
                                } else {
                                    stringResource(R.string.feature_subscription_subscribe_button)
                                }
                                append(text)
                                append(" ")
                                append(
                                    stringResource(
                                        id = R.string.feature_subscription_etb,
                                        subscriptionPrice
                                    )
                                )

                            }
                        }
                    )
                },
                isEnabled = isButtonEnabled,
                isProcessing = isSubscriptionProcessing
            )
        },
        modifier = modifier
    )
}


@Composable
fun BillingType(
    onBillingTypeSelected: (SubscriptionBillingType) -> Unit,
    billingType: SubscriptionBillingType
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(billingType) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(id = R.string.feature_subscription_billing_type_label))
        Spacer(modifier = Modifier.height(8.dp))
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            content = {
                Column {
                    CustomInputButton(
                        text = stringResource(id = selected.title),
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        leadingIcon = CrbtIcons.PaymentMethods
                    )
                    if (expanded) {
                        SubscriptionBillingType.entries.forEach {
                            ListItem(
                                headlineContent = { Text(text = stringResource(id = it.title)) },
                                modifier = Modifier.clickable {
                                    selected = it
                                    expanded = false
                                    onBillingTypeSelected(it)
                                },
                            )
                        }
                    }
                }
            },
            color = MaterialTheme.colorScheme.outlineVariant.copy(
                stronglyDeemphasizedAlpha,
            )
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDate(
    onDatePicked: (Long) -> Unit,
    date: Long?,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault())
    var pickedDate by remember { mutableStateOf(date) }

    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dateString = pickedDate?.let { dateFormat.format(it) }
        ?: stringResource(id = R.string.feature_subscription_date_placeholder)

    CustomInputButton(
        text = dateString,
        leadingIcon = CrbtIcons.Calendar,
        onClick = { showDatePicker = true },
        trailingIcon = {
            Icon(
                imageVector = CrbtIcons.ArrowRight,
                contentDescription = CrbtIcons.ArrowRight.name,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    )

    AnimatedVisibility(
        visible = showDatePicker,
        enter = expandVertically(),
        exit = shrinkOut()
    ) {
        ShowDatePicker(
            title = stringResource(id = R.string.feature_subscription_date_title),
            onDateSelected = {
                onDatePicked(it)
                pickedDate = it
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
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
            songProfileUrl = song.profile
        )
    }
}

@ThemePreviews
@Composable
fun CrbtSubscribeContentPreview() {
    CrbtTheme {
        SubscribeContent(
            isGiftSubscription = false,
            onBillingTypeSelected = {},
            billingType = SubscriptionBillingType.Monthly,
            onDatePicked = {},
            date = null,
            onSubscribeClick = {},
            subscriptionPrice = 10.30,
            isSubscriptionProcessing = false,
            isButtonEnabled = true
        )
    }
}

@ThemePreviews
@Composable
fun CrbtSubscribeContentPreview2() {
    CrbtTheme {
        SubscribeContent(
            isGiftSubscription = true,
            onBillingTypeSelected = {},
            billingType = SubscriptionBillingType.Monthly,
            onDatePicked = {},
            date = null,
            onSubscribeClick = {},
            subscriptionPrice = 10.30,
            isSubscriptionProcessing = false,
            isButtonEnabled = true
        )
    }
}