package com.crbt.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.designsystem.components.SurfaceCard
import com.crbt.designsystem.icon.CrbtIcons
import com.example.crbtjetcompose.core.ui.R

@Composable
fun QuickAccess(
    navigateToPackages: () -> Unit,
    navigateToRecharge: () -> Unit,
    navigateToServices: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = com.example.crbtjetcompose.feature.home.R.string.feature_home_quick_services),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Row(
                modifier = Modifier
                    .clickable { navigateToServices() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = com.example.crbtjetcompose.feature.home.R.string.feature_home_view_all),
                )
                Icon(
                    imageVector = CrbtIcons.ArrowRight,
                    contentDescription = CrbtIcons.ArrowRight.name,
                    Modifier
                        .clickable { navigateToServices() },
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SurfaceCard(
                content = {
                    QuickAccessItem(
                        title = stringResource(id = R.string.core_ui_packages),
                        icon = CrbtIcons.Packages,
                        onClick = navigateToPackages
                    )
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.size(16.dp))

            SurfaceCard(
                content = {
                    QuickAccessItem(
                        title = stringResource(id = R.string.core_ui_recharge),
                        icon = CrbtIcons.Recharge,
                        onClick = navigateToRecharge
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickAccessItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun PreviewQuickAccess() {
    QuickAccess(
        navigateToPackages = {},
        navigateToRecharge = {},
        navigateToServices = {}
    )
}