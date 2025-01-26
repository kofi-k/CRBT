package com.crbt.subscription

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import com.crbt.LikeableToneCategoriesUiState
import com.crbt.designsystem.icon.CrbtIcons


@Composable
fun InterestedCategorySelection(
    categoriesUiState: LikeableToneCategoriesUiState.Shown,
    onCategoryCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = trace("InterestedCategorySelection") {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(
                items = categoriesUiState.toneCategories,
                key = { it.toneCategory },
            ) {
                SingleToneCategoryButton(
                    categoryName = it.toneCategory,
                    isSelected = it.isInterestedInCategory,
                    onClick = onCategoryCheckedChanged,
                )
            }
        }
    }
}

@Composable
private fun SingleToneCategoryButton(
    categoryName: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit,
) = trace("SingleToneCategoryButton") {
    Surface(
        modifier = Modifier
            .animateContentSize()
            .wrapContentWidth(),
        shape = CircleShape,
        color = if (isSelected)
            MaterialTheme.colorScheme.primary else
            MaterialTheme.colorScheme.surfaceContainer,
        selected = isSelected,
        onClick = {
            onClick(categoryName, !isSelected)
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .padding(vertical = 4.dp),
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary else
                    MaterialTheme.colorScheme.onSurface,
            )
            if (isSelected) {
                Icon(
                    imageVector = CrbtIcons.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(start = 6.dp),
                )
            }
        }
    }
}