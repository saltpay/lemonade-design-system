package com.teya.lemonade

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

private interface ScrollStateAdapter {
    val firstVisibleItemIndex: Int
    val firstVisibleItemScrollOffset: Int
}

private class ListStateAdapter(
    private val state: LazyListState
) : ScrollStateAdapter {
    override val firstVisibleItemIndex get() = state.firstVisibleItemIndex
    override val firstVisibleItemScrollOffset get() = state.firstVisibleItemScrollOffset
}

private class GridStateAdapter(
    private val state: LazyGridState
) : ScrollStateAdapter {
    override val firstVisibleItemIndex get() = state.firstVisibleItemIndex
    override val firstVisibleItemScrollOffset get() = state.firstVisibleItemScrollOffset
}

private class ColumnStateAdapter(
    private val state: ScrollState
) : ScrollStateAdapter {
    override val firstVisibleItemIndex: Int get() = 0
    override val firstVisibleItemScrollOffset: Int get() = state.value
}

@Composable
private fun rememberCollapseProgress(
    listState: ScrollStateAdapter,
    collapseDistance: Dp = 64.dp
): Float {
    val density = LocalDensity.current
    val collapsePx = with(density) { collapseDistance.toPx() }

    return remember(listState, density) {
        derivedStateOf {
            val offset = when {
                listState.firstVisibleItemIndex > 0 -> collapsePx
                else -> listState.firstVisibleItemScrollOffset.toFloat()
            }
            (offset / collapsePx).coerceIn(0f, 1f)
        }
    }.value
}

@Composable
internal fun SampleScreenDisplayLazyColumn(
    title: String,
    modifier: Modifier = Modifier,
    contentHorizontalPadding: Dp = LemonadeTheme.spaces.spacing400,
    background: Color = LemonadeTheme.colors.background.bgSubtle,
    header: @Composable (progress: Float) -> Unit = { progress ->
        SampleScreenHeader(title = title, progress = progress)
    },
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()

    val bottomGesturePadding = (WindowInsets.safeGestures.getBottom(density).dp / 2)

    val progress = rememberCollapseProgress(ListStateAdapter(listState))
    var headerHeightDp by remember { mutableStateOf(0.dp) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(top = LemonadeTheme.spaces.spacing400),
            contentPadding = PaddingValues(
                top = headerHeightDp,
                start = contentHorizontalPadding,
                end = contentHorizontalPadding,
                bottom = bottomGesturePadding
            ),
            content = content
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .onSizeChanged {
                headerHeightDp = with(density) { it.height.toDp() }
            }
    ) {
        header(progress)
    }
}

@Composable
internal fun SampleScreenDisplayLazyGrid(
    title: String,
    modifier: Modifier = Modifier,
    contentHorizontalPadding: Dp = LemonadeTheme.spaces.spacing400,
    background: Color = LemonadeTheme.colors.background.bgSubtle,
    header: @Composable (progress: Float) -> Unit = { progress ->
        SampleScreenHeader(title = title, progress = progress)
    },
    columns: GridCells = GridCells.Adaptive(100.dp),
    columnsGap: Dp = LemonadeTheme.spaces.spacing200,
    content: LazyGridScope.() -> Unit
) {
    val density = LocalDensity.current
    val bottomGesturePadding = (WindowInsets.safeGestures.getBottom(density).dp / 2)

    val listState = rememberLazyGridState()

    val progress = rememberCollapseProgress(GridStateAdapter(listState))
    var headerHeightDp by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
    ) {
        Spacer(modifier = Modifier.height(headerHeightDp))

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(background)
        ) {
            LazyVerticalGrid(
                state = listState,
                columns = columns,
                contentPadding = PaddingValues(
                    start = contentHorizontalPadding,
                    end = contentHorizontalPadding,
                    bottom = bottomGesturePadding
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    space = columnsGap,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(
                    space = columnsGap,
                    alignment = Alignment.CenterVertically
                ),
                content = content
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .onSizeChanged {
                headerHeightDp = with(density) { it.height.toDp() }
            }
    ) {
        header(progress)
    }
}

@Composable
internal fun SampleScreenDisplayColumn(
    title: String,
    modifier: Modifier = Modifier,
    contentHorizontalPadding: Dp = LemonadeTheme.spaces.spacing400,
    contentBottomPadding: Dp = LemonadeTheme.spaces.spacing400,
    background: Color = LemonadeTheme.colors.background.bgSubtle,
    itemsSpacing: Dp = LemonadeTheme.spaces.spacing300,
    header: @Composable (progress: Float) -> Unit = { progress ->
        SampleScreenHeader(title = title, progress = progress)
    },
    content: @Composable ColumnScope.() -> Unit
) {

    val density = LocalDensity.current
    val listState = rememberScrollState()

    val progress = rememberCollapseProgress(ColumnStateAdapter(listState))
    var headerHeightDp by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = listState)
                .navigationBarsPadding()
                .background(background)
                .padding(
                    PaddingValues(
                        top = headerHeightDp,
                        start = contentHorizontalPadding,
                        end = contentHorizontalPadding,
                        bottom = contentBottomPadding
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(itemsSpacing),
            content = content
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .onSizeChanged {
                headerHeightDp = with(density) { it.height.toDp() }
            }
    ) {
        header(progress)
    }
}


