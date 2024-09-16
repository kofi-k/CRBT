package com.crbt.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.crbt.common.core.common.result.Result
import com.crbt.data.core.data.util.copyImageToInternalStorage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.designsystem.theme.stronglyDeemphasizedAlpha
import com.crbt.ui.core.ui.EmailCheck
import com.crbt.ui.core.ui.OnboardingSheetContainer
import com.crbt.ui.core.ui.UsernameDetails
import com.example.crbtjetcompose.core.model.data.CrbtUser
import com.example.crbtjetcompose.feature.profile.R


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val userResult by profileViewModel.userResultState.collectAsStateWithLifecycle()

    when (userResult) {
        is Result.Loading -> CircularProgressIndicator()
        is Result.Error -> Unit
        is Result.Success -> {
            ProfileContent(
                modifier = modifier,
                userData = (userResult as Result.Success<CrbtUser>).data,
                onSaveButtonClicked = onSaveButtonClicked,
                saveProfile = { firstName, lastName ->
                    profileViewModel.saveProfile(firstName, lastName)
                },
                saveProfileImage = { profileUrl ->
                    profileViewModel.saveProfileImage(profileUrl)
                },
            )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userData: CrbtUser,
    onSaveButtonClicked: () -> Unit,
    saveProfile: (firstName: String, lastName: String) -> Unit,
    saveProfileImage: (String) -> Unit,
) {
    var profileImage by rememberSaveable {
        mutableStateOf(Uri.parse(userData.profileUrl))
    }
    val context = LocalContext.current
    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                profileImage = uri
            }
        },
    )
    var firstName by rememberSaveable {
        mutableStateOf(userData.firstName)
    }
    var lastName by rememberSaveable {
        mutableStateOf(userData.lastName)
    }
    var isButtonEnabled by rememberSaveable {
        mutableStateOf(firstName.isNotBlank() && lastName.isNotBlank())
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.feature_profile_setup_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                profileImage = it
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
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
                    onEmailCheckChanged = { /*todo handle with vm*/ },
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProcessButton(
            onClick = {
                saveProfile(firstName, lastName)
                if (profileImage != Uri.EMPTY) {
                    saveProfileImage(
                        copyImageToInternalStorage(
                            context,
                            profileImage
                        ).toString()
                    )
                }
                onSaveButtonClicked()
            },
            isEnabled = isButtonEnabled,
            modifier = modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.feature_profile_save_profile_button)
        )
    }
}

@Composable
fun UserProfileImage(
    profileImage: Uri,
    onPickImage: () -> Unit,
    onRemoveImage: (Uri) -> Unit,
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
        targetValue = if (profileImage != Uri.EMPTY) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ),
        label = "animatePickedProfileImageState",
    )

    val imageModifier = if (profileImage != Uri.EMPTY) {
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
                    color = if (profileImage == Uri.EMPTY)
                        MaterialTheme.colorScheme.surface
                    else Color.Transparent
                )
                .clickable(onClick = onPickImage)
                .then(modifier),
        ) {
            if (profileImage != Uri.EMPTY) {
                AsyncImage(
                    contentScale = ContentScale.Crop,
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(profileImage)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentDescription = "image",
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

        val icon = if (profileImage != Uri.EMPTY) CrbtIcons.Delete else CrbtIcons.AddPhoto
        val colors = if (profileImage != Uri.EMPTY) {
            IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        } else {
            IconButtonDefaults.filledIconButtonColors()
        }
        val animteXOffset by animateDpAsState(
            targetValue = if (profileImage == Uri.EMPTY) 0.dp else (-18).dp,
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
                    if (profileImage != Uri.EMPTY) {
                        onRemoveImage(Uri.EMPTY)
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