package com.crbt.subscription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import com.crbt.data.core.data.SubscriptionCategories.categories
import com.itengs.crbt.core.model.data.SubscriptionCategory

@Composable
fun Categories(
    onCategoryClick: (String) -> Unit,
) {
    CategoriesRow(
        onCategoryClick = onCategoryClick,
        categories = categories
    )
}

@Composable
fun CategoriesRow(
    onCategoryClick: (String) -> Unit,
    categories: List<SubscriptionCategory>
) = trace("CategoriesRow") {

    val lazyListState = rememberLazyListState()
    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
        ),
        modifier = Modifier
            .heightIn(max = max(50.dp, with(LocalDensity.current) { 50.sp.toDp() }))
            .fillMaxWidth()
    ) {
        items(
            items = categories,
            key = { category -> category.id }
        ) { category ->
            Button(
                onClick = { onCategoryClick(category.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = category.name)
            }
        }
    }
}

@Preview
@Composable
fun CategoriesPreview() {
    Categories(
        onCategoryClick = {},
    )
}