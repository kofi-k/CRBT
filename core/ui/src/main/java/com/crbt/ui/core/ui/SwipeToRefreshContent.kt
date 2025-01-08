import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshContent(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(
                state = refreshState,
            )
    ) {
        content()
        PullRefreshIndicator(
            isRefreshing,
            refreshState,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 34.dp)
        )
    }
}
