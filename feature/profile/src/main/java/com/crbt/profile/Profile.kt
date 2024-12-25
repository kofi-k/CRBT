package com.crbt.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.rectangle
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crbt.data.core.data.repository.UpdateUserInfoUiState
import com.crbt.data.core.data.util.convertImageToBase64
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.domain.UserPreferenceUiState
import com.crbt.ui.core.ui.EmailCheck
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.UsernameDetails
import com.crbt.ui.core.ui.validationStates.isValidEmail
import com.example.crbtjetcompose.core.model.data.CrbtUser
import com.example.crbtjetcompose.core.model.data.asCrbtUser
import com.example.crbtjetcompose.feature.profile.R
import kotlinx.coroutines.launch


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val userPreferenceUiState by profileViewModel.userPreferenceUiState.collectAsStateWithLifecycle()
    val userInfoUiState = profileViewModel.userInfoUiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    when (userPreferenceUiState) {
        is UserPreferenceUiState.Loading -> CircularProgressIndicator()
        is UserPreferenceUiState.Success -> {
            ProfileContent(
                modifier = modifier,
                userData = (userPreferenceUiState as UserPreferenceUiState.Success).userData.asCrbtUser(),
                saveProfile = { firstName, lastName, email, profile ->
                    profileViewModel.saveProfile(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        profile = profile,
                        onSuccessfulUpdate = {
                            onSaveButtonClicked()
                        },
                        onFailedUpdate = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    it,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                },
                isSaving = userInfoUiState is UpdateUserInfoUiState.Loading,
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userData: CrbtUser,
    saveProfile: (firstName: String, lastName: String, email: String, profile: String) -> Unit,
    isSaving: Boolean,
) {
    var profileImage by rememberSaveable {
        mutableStateOf(userData.profileUrl)
    }
    val context = LocalContext.current
    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                profileImage = convertImageToBase64(context, uri) ?: ""
            }
        },
    )
    var firstName by rememberSaveable {
        mutableStateOf(userData.firstName)
    }
    var lastName by rememberSaveable {
        mutableStateOf(userData.lastName)
    }

    var userEmailAddress by rememberSaveable {
        mutableStateOf(userData.email)
    }
    var checked by remember {
        mutableStateOf(userData.email.isNotBlank())
    }
    var isButtonEnabled by rememberSaveable {
        mutableStateOf(firstName.isNotBlank() && lastName.isNotBlank())
    }
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 16.dp
        )
    ) {
        item {
            Text(
                text = stringResource(id = R.string.feature_profile_setup_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        item {
            UserProfileImage(
                profileImage = profileImage,
                onPickImage = {
                    pickPhoto.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
                onRemoveImage = {
                    profileImage = ""
                },
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.height(24.dp))
            UsernameDetails(
                modifier = modifier,
                onUserProfileResponse = { fName, lName, isValid ->
                    firstName = fName
                    lastName = lName
                    isButtonEnabled = isValid
                },
                initialFirstName = firstName,
                initialLastName = lastName,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OnboardingSheetContainer(
                title = stringResource(id = R.string.feature_profile_updates_title),
                subtitle = stringResource(id = R.string.feature_profile_updates_subtitle),
                content = {
                    EmailCheck(
                        modifier = modifier,
                        onEmailCheckChanged = {
                            checked = it
                            if (!checked) {
                                userEmailAddress = ""
                            }
                        },
                        checked = checked,
                        userEmailAddress = userEmailAddress,
                        onEmailChanged = { email, isValid ->
                            userEmailAddress = email
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProcessButton(
                onClick = {
                    saveProfile(
                        firstName,
                        lastName,
                        userEmailAddress,
                        profileImage
                    )
                },
                isEnabled = if (checked) {
                    userEmailAddress.isValidEmail() && isButtonEnabled
                } else {
                    isButtonEnabled
                },
                modifier = modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.feature_profile_save_profile_button),
                isProcessing = isSaving
            )
        }
    }
}

@Composable
fun UserProfileImage(
    profileImage: String,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val shapeA = remember {
        RoundedPolygon.circle()
    }
    val shapeB = remember {
        RoundedPolygon.rectangle(
            rounding = CornerRounding(0.5f),
        )
    }

    val morph = remember {
        Morph(shapeA, shapeB)
    }

    val animatePickedProfileImageState = animateFloatAsState(
        targetValue = if (profileImage.isNotBlank()) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ),
        label = "animatePickedProfileImageState",
    )

    val imageModifier = if (profileImage.isNotBlank()) {
        Modifier
            .fillMaxWidth()
            .size(260.dp)
    } else {
        Modifier.size(200.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .then(imageModifier)
                .clip(MorphPolygonShape(morph, animatePickedProfileImageState.value))
                .background(
                    color = if (profileImage.isBlank())
                        MaterialTheme.colorScheme.surface
                    else Color.Transparent
                )
                .clickable(onClick = onPickImage)
                .then(modifier),
        ) {
            if (profileImage.isNotBlank()) {
                DynamicAsyncImage(
                    base64ImageString = profileImage,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            } else {
                Icon(
                    imageVector = CrbtIcons.Person,
                    contentDescription = CrbtIcons.Person.name,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(160.dp),
                )
            }
        }

        val icon = if (profileImage.isNotBlank()) CrbtIcons.Delete else CrbtIcons.AddPhoto
        val colors = if (profileImage.isNotBlank()) {
            IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        } else {
            IconButtonDefaults.filledIconButtonColors()
        }
        val animteXOffset by animateDpAsState(
            targetValue = if (profileImage.isBlank()) 0.dp else (-18).dp,
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing,
            ),
            label = "animteXOffset",
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset {
                    IntOffset(animteXOffset.roundToPx(), (-24))
                }
        ) {
            FilledIconButton(
                onClick = {
                    if (profileImage.isNotBlank()) {
                        onRemoveImage()
                    } else {
                        onPickImage()
                    }
                },
                colors = colors,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        }
    }
}

class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float
) : Shape {

    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // Below assumes that you haven't changed the default radius of 1f, nor the centerX and centerY of 0f
        // By default this stretches the path to the size of the container, if you don't want stretching, use the same size.width for both x and y.
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)

        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }
}


@ThemePreviews
@Composable
fun ProfilePreview() {
    CrbtTheme {
        Profile(
            onSaveButtonClicked = {},
        )
    }
}