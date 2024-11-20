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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.DummyTones
import com.crbt.data.core.data.SubscriptionBillingType
import com.crbt.data.core.data.repository.UssdUiState
import com.crbt.data.core.data.util.simpleDateFormatPattern
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.CustomGradientColors
import com.crbt.designsystem.theme.bodyFontFamily
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.CustomInputButton
import com.crbt.ui.core.ui.GiftPurchasePhoneNumber
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.ShowDatePicker
import com.example.crbtjetcompose.feature.subscription.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun CrbtSubscribeScreen(
    onSubscribeSuccess: () -> Unit,
    onBackClicked: () -> Unit,
    subscriptionViewModel: SubscriptionViewModel = hiltViewModel(),
) {

    val isGiftSub by subscriptionViewModel.isGiftSubscription.collectAsStateWithLifecycle()
    val crbtSong by subscriptionViewModel.crbtSongResource.collectAsStateWithLifecycle()
    val subscriptionUiState by subscriptionViewModel.subscriptionUiState.collectAsStateWithLifecycle()
    val isUserRegisteredForCrbt by subscriptionViewModel.isUserRegisteredForCrbt.collectAsStateWithLifecycle()
    val ussdState by subscriptionViewModel.ussdState.collectAsStateWithLifecycle()

    var showRegistrationDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    LaunchedEffect(subscriptionUiState) {
        when (subscriptionUiState) {
            is SubscriptionUiState.Success -> onSubscribeSuccess()

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

        MusicInfo(
            price = crbtSong?.price ?: "0.00",
            numberOfSubscribers = crbtSong?.numberOfSubscribers ?: 0,
            numberOfPlays = crbtSong?.numberOfListeners ?: 0,
            billingType = "/ ${crbtSong?.subscriptionType?.lowercase()}"
        )

        SubscribeContent(
            isGiftSubscription = isGiftSub ?: false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            onSubscribeClick = {
                if (!isUserRegisteredForCrbt) {
                    showRegistrationDialog = true
                } else {
                    subscriptionViewModel.subscribeToTone(
                        ussdCode = crbtSong?.ussdCode ?: "",
                        activity = context as Activity,
                    )
                }
            },
            subscriptionPrice = crbtSong?.price?.toDoubleOrNull() ?: 0.00,
            isSubscriptionProcessing = subscriptionUiState == SubscriptionUiState.Loading,
            isButtonEnabled = crbtSong != null,
            onGiftPhoneNumberChanged = subscriptionViewModel::onPhoneNumberChange
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showRegistrationDialog) {
        CrbtRegistrationDialog(
            onDismiss = {
                subscriptionViewModel.updateUserCrbtSubscriptionStatus()
            },
            onRegister = {
                subscriptionViewModel.runUssdCode(
                    ussdCode = crbtSong?.registrationUssdCode ?: "",
                    onSuccess = {
                        subscriptionViewModel.updateUserCrbtSubscriptionStatus()
                    },
                    onError = {},
                    activity = context as Activity
                )
            },
            isRegistering = ussdState is UssdUiState.Loading || (
                    ussdState is UssdUiState.Success &&
                            subscriptionUiState is SubscriptionUiState.Loading
                    ),
            isUpdatingRegisterStatus = subscriptionUiState is SubscriptionUiState.Loading &&
                    ussdState is UssdUiState.Idle
        )
    }
}

@Composable
fun CrbtRegistrationDialog(
    onDismiss: () -> Unit,
    onRegister: () -> Unit,
    isRegistering: Boolean,
    isUpdatingRegisterStatus: Boolean
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(id = R.string.feature_subscription_new_to_crbt),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.feature_subscription_register_for_crbt_description),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                ProcessButton(
                    onClick = onRegister,
                    colors = ButtonDefaults.textButtonColors(),
                    text = stringResource(id = R.string.feature_subscription_register_now),
                    isProcessing = isRegistering
                )

                ProcessButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(),
                    text = stringResource(id = R.string.feature_subscription_already_registered),
                    isProcessing = isUpdatingRegisterStatus
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )

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
            imageRes = R.drawable.feature_subscription_onboardingbackground
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
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
fun MusicInfo(
    price: String,
    numberOfSubscribers: Int,
    numberOfPlays: Int,
    billingType: String
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
    onGiftPhoneNumberChanged: (String) -> Unit,
    onSubscribeClick: () -> Unit,
    subscriptionPrice: Double,
    isSubscriptionProcessing: Boolean,
    isButtonEnabled: Boolean
) {
    val title = if (isGiftSubscription) {
        stringResource(
            id = R.string.feature_subscription_gift_song_title, ""
        ) to stringResource(
            id = R.string.feature_subscription_gift_song_subtitle
        )
    } else {
        stringResource(
            id = R.string.feature_subscription_subscribe_to_song_title,
            ""
        ) to stringResource(
            id = R.string.feature_subscription_subscribe_to_song_subtitle
        )
    }
    var isPhoneNumberValid by remember { mutableStateOf(false) }

    OnboardingSheetContainer(
        title = title.first,
        subtitle = title.second,
        content = {
            if (isGiftSubscription) {
                Spacer(modifier = Modifier.height(8.dp))
                GiftPurchasePhoneNumber(
                    onPhoneNumberChanged = { phoneNumber, isValid ->
                        isPhoneNumberValid = isValid
                        onGiftPhoneNumberChanged(phoneNumber)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                isEnabled = if (isGiftSubscription) isPhoneNumberValid else isButtonEnabled,
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
            onSubscribeClick = {},
            subscriptionPrice = 10.30,
            isSubscriptionProcessing = false,
            isButtonEnabled = true,
            onGiftPhoneNumberChanged = { _ -> }
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
            isButtonEnabled = true,
            onGiftPhoneNumberChanged = { _ -> }
        )
    }
}