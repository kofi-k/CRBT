package com.crbt.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import com.crbt.data.core.data.CRBTSettingsData
import com.crbt.designsystem.components.ListCard
import com.crbt.designsystem.components.ThemePreviews
import com.crbt.designsystem.components.scrollbar.DecorativeScrollbar
import com.crbt.designsystem.components.scrollbar.scrollbarState
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.CrbtTheme
import com.crbt.ui.core.ui.permissionIcons
import com.example.crbtjetcompose.feature.profile.R

@Composable
fun PermissionSettings(
    onPermissionCheckChange: (String, Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotateIcon by animateFloatAsState(if (expanded) 90f else 0f, label = "")
    var allowedPermissions by remember { mutableStateOf(setOf<String>()) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListCard(
            onClick = { expanded = !expanded },
            headlineText = stringResource(id = R.string.feature_profile_permissions),
            subText = stringResource(id = R.string.feature_profile_permissions_description),
            leadingContentIcon = CrbtIcons.Permissions,
            trailingContent = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = CrbtIcons.ArrowRight,
                        contentDescription = CrbtIcons.ArrowRight.name,
                        modifier = Modifier.rotate(rotateIcon)
                    )
                }
            }
        )
        if (expanded) {
            AllowedPermissions(
                onPermissionCheckChange = { permissionId, isSelected ->
                    allowedPermissions = if (isSelected) {
                        allowedPermissions.plus(permissionId)
                    } else {
                        allowedPermissions.minus(permissionId)
                    }
                    onPermissionCheckChange(permissionId, isSelected)
                },
                allowedPermissions = allowedPermissions
            )
        }
    }
}


@Composable
fun AllowedPermissions(
    onPermissionCheckChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    allowedPermissions: Set<String>
) = trace("AllowedPermissions") {
    val lazyGridState = rememberLazyGridState()

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier
                .heightIn(max = max(200.dp, with(LocalDensity.current) { 200.sp.toDp() }))
                .fillMaxWidth(),
        ) {
            items(
                items = CRBTSettingsData.permissions,
                key = { it.id }
            ) {
                SinglePermissionButton(
                    name = stringResource(it.name),
                    permissionId = it.id,
                    icon = permissionIcons[it.name] ?: CrbtIcons.Permissions,
                    isSelected = it.id in allowedPermissions,
                    onClick = onPermissionCheckChange
                )
            }
        }
        lazyGridState.DecorativeScrollbar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .align(Alignment.BottomStart),
            state = lazyGridState.scrollbarState(itemsAvailable = CRBTSettingsData.permissions.size),
            orientation = Orientation.Horizontal,
        )
    }
}

@Composable
private fun SinglePermissionButton(
    name: String,
    permissionId: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
) = trace("SinglePermissionButton") {
    Surface(
        modifier = Modifier
            .width(220.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surfaceBright,
        selected = isSelected,
        onClick = {
            onClick(permissionId, !isSelected)
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = {
                    onClick(permissionId, it)
                }
            )
        }
    }
}



@ThemePreviews
@Composable
fun PreviewAllowedPermissions() {
    CrbtTheme {
        AllowedPermissions(
            onPermissionCheckChange = { _, _ -> },
            allowedPermissions = setOf("1", "2", "3")
        )
    }
}