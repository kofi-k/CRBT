package com.crbt.profile

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crbt.data.core.data.util.copyRightInfo
import com.crbt.data.core.data.util.getAppVersion
import com.crbt.designsystem.components.DynamicAsyncImage
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.icon.CrbtIcons
import com.crbt.designsystem.theme.onPrimaryDark
import com.crbt.designsystem.theme.primaryDark
import com.crbt.designsystem.theme.slightlyDeemphasizedAlpha
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.itengs.crbt.feature.profile.R


@Composable
fun AppInfo(
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        DynamicAsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            imageUrl = "",
            imageRes = com.itengs.crbt.core.ui.R.drawable.core_ui_pattern_bg,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(0.75f)
            })
        )

        IconButton(
            onClick = navigateUp,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 36.dp, start = 16.dp)
                .align(Alignment.TopStart),
        ) {
            Icon(
                imageVector = CrbtIcons.ArrowBack,
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = com.itengs.crbt.core.ui.R.string.core_ui_app_name),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(
                    id = R.string.feature_profile_app_version,
                    getAppVersion(context)
                ),
                color = Color.White.copy(alpha = slightlyDeemphasizedAlpha),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(16.dp))

            DynamicAsyncImage(
                modifier = Modifier.size(80.dp),
                imageUrl = "",
                imageRes = com.itengs.crbt.core.ui.R.drawable.core_ui_logo,
                colorFilter = ColorFilter.tint(Color.White),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.padding(16.dp))

            val devTeam = stringResource(id = com.itengs.crbt.core.data.R.string.core_data_dev_team)
            Text(
                text = stringResource(
                    id = R.string.feature_profile_copy_right,
                    copyRightInfo(devTeam = devTeam)
                ),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(16.dp))

            ProcessButton(
                onClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
                modifier = Modifier
                    .wrapContentSize(),
                text = stringResource(id = R.string.feature_profile_licenses),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryDark,
                    contentColor = onPrimaryDark
                )
            )

        }

    }
}

@Preview
@Composable
fun AppInfoPreview() {
    AppInfo(navigateUp = {})
}