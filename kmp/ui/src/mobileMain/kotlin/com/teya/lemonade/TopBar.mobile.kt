package com.teya.lemonade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TopBarAction
import com.teya.lemonade.core.TopBarVariant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.math.roundToInt

/**
 * A collapsible top bar component that displays a large title which collapses
 * into a smaller inline title as the user scrolls through content.
 *
 * The TopBar works with external scrollable content through [NestedScrollConnection].
 * Use [rememberTopBarState] to create the state and apply its [TopBarState.nestedScrollConnection]
 * [TopBarState.nestedScrollConnection] to your scrollable content via
 * [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
 *
 * ## Usage
 * ```kotlin
 * val topBarState = rememberTopBarState()
 *
 * Column {
 *     LemonadeUi.TopBar(
 *         label = "Screen Title",
 *         state = topBarState,
 *         variant = TopBarVariant.Default,
 *         leadingSlot = {
 *             LemonadeUi.IconButton(
 *                 icon = LemonadeIcons.ChevronLeft,
 *                 contentDescription = "Back",
 *                 onClick = { /* handle back */ }
 *             )
 *         },
 *         trailingSlot = {
 *             LemonadeUi.IconButton(
 *                 icon = LemonadeIcons.Settings,
 *                 contentDescription = "Settings",
 *                 onClick = { /* handle settings */ }
 *             )
 *         },
 *     )
 *
 *     LazyColumn(
 *         modifier = Modifier.nestedScroll(topBarState.nestedScrollConnection)
 *     ) {
 *         items(100) { index ->
 *             Text("Item $index")
 *         }
 *     }
 * }
 * ```
 *
 * @param label The title text displayed in both expanded (large) and collapsed (small - if [collapsedLabel] is null) states.
 * @param collapsedLabel The title text displayed in collapsed (small) state. If not set it will display [label] instead.
 * @param state The [TopBarState] that manages collapse behavior. Create with [rememberTopBarState].
 * @param variant Visual variant of the top bar. See [TopBarVariant].
 * @param navigationAction Visual variant of the top bar's action. See [TopBarAction].
 * @param onNavigationActionClicked Callback triggered when the [navigationAction] visual representation is clicked.
 * @param modifier [Modifier] applied to the top bar container.
 * @param trailingSlot Optional composable displayed at the end of the fixed header (typically action buttons).
 * @param bottomSlot Optional composable displayed below the expanded title. This content remains
 *        visible and acts as a sticky area when fully collapsed.
 */
@Composable
public fun LemonadeUi.TopBar(
    label: String,
    variant: TopBarVariant,
    modifier: Modifier = Modifier,
    state: TopBarState = rememberTopBarState(),
    collapsedLabel: String? = null,
    navigationAction: TopBarAction? = null,
    onNavigationActionClicked: (() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (BoxScope.() -> Unit)? = null,
) {
    CoreTopBar(
        state = state,
        variant = variant,
        modifier = modifier,
        bottomSlot = bottomSlot,
        fixedHeaderSlot = { headerModifier ->
            CoreTopBarContent(
                leadingSlot = {
                    if (navigationAction != null && onNavigationActionClicked != null) {
                        CoreTopBarActionContent(
                            navigationAction = navigationAction,
                            onNavigationActionClicked = onNavigationActionClicked,
                            modifier = Modifier.matchParentSize(),
                        )
                    }
                },
                trailingSlot = trailingSlot,
                label = collapsedLabel ?: label,
                labelAlpha = state.collapseProgress,
                variant = variant,
                modifier = headerModifier
                    .background(color = variant.backgroundColor)
                    .zIndex(zIndex = 1f)
                    .padding(
                        horizontal = LocalSpaces.current.spacing200,
                        vertical = LocalSpaces.current.spacing50,
                    ),
            )
        },
        collapsableSlot = { expandedModifier ->
            Box(
                modifier = expandedModifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpaces.current.spacing400)
                    .padding(
                        top = LocalSpaces.current.spacing50,
                        bottom = LocalSpaces.current.spacing200,
                    ),
            ) {
                LemonadeUi.Text(
                    text = label,
                    textStyle = LocalTypographies.current.headingLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            }
        },
    )
}

/**
 * Creates and remembers a [TopBarState] instance.
 *
 * @param startCollapsed When `true`, the top bar starts in the collapsed state.
 *        The collapsable content will be hidden and the inline title will be visible immediately.
 *        Defaults to `false`.
 * @param coroutineScope The [CoroutineScope] used for scroll-offset animations. Defaults to
 *        [rememberCoroutineScope].
 * @param startWithLockedGestureAnimation When `true`, scroll gestures from nested scrollable content
 *        will not collapse or expand the top bar. The bar can still be collapsed or
 *        expanded programmatically via [TopBarState.collapse] and [TopBarState.expand].
 *        Defaults to `false`.
 * @return A remembered [TopBarState] instance.
 *
 * ## Usage
 * ```kotlin
 * val topBarState = rememberTopBarState(
 *     startCollapsed = true,
 *     startWithLockedGestureAnimation = true,
 * )
 *
 * Column {
 *     LemonadeUi.TopBar(
 *         label = "Screen Title",
 *         state = topBarState,
 *         variant = TopBarVariant.Default,
 *     )
 *
 *     LazyColumn(
 *         modifier = Modifier.nestedScroll(topBarState.nestedScrollConnection)
 *     ) {
 *         // Your content here
 *     }
 * }
 * ```
 */

@Composable
public fun rememberTopBarState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startCollapsed: Boolean = false,
    startWithLockedGestureAnimation: Boolean = false,
): TopBarState {
    return remember(startCollapsed, startWithLockedGestureAnimation) {
        TopBarState(
            coroutineScope = coroutineScope,
            startCollapsed = startCollapsed,
            startWithLockedGestureAnimation = startWithLockedGestureAnimation,
        )
    }
}

/**
 * A collapsible top bar with an integrated search field that collapses into the bar
 * as the user scrolls through content.
 *
 * When the search field gains focus, the fixed header (title and actions) animates out,
 * scroll-gesture animations are locked, the top bar expands, and the [bottomSlot] becomes visible.
 * Tapping the leading icon clears focus and restores the default state.
 *
 * Use [rememberTopBarState] to create the state and apply its [TopBarState.nestedScrollConnection]
 * to your scrollable content via
 * [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
 *
 * ## Usage
 * ```kotlin
 * val topBarState = rememberTopBarState()
 * var query by remember { mutableStateOf("") }
 *
 * Column {
 *     LemonadeUi.SearchTopBar(
 *         label = "Search",
 *         state = topBarState,
 *         variant = TopBarVariant.Default,
 *         searchInput = query,
 *         onSearchChanged = { query = it },
 *         navigationAction = TopBarAction.Back,
 *         onNavigationActionClicked = { /* handle back */ },
 *     )
 *
 *     LazyColumn(
 *         modifier = Modifier.nestedScroll(topBarState.nestedScrollConnection)
 *     ) {
 *         // search results
 *     }
 * }
 * ```
 *
 * @param label The title text displayed in the fixed header.
 * @param variant Visual variant of the top bar. See [TopBarVariant].
 * @param searchInput The current search query text.
 * @param onSearchChanged Callback invoked when the search query changes.
 * @param modifier [Modifier] applied to the top bar container.
 * @param state The [TopBarState] that manages collapse behavior. Create with [rememberTopBarState].
 * @param navigationAction Optional action icon displayed in the header. See [TopBarAction].
 * @param onNavigationActionClicked Callback triggered when [navigationAction] is clicked.
 * @param trailingSlot Optional composable displayed at the end of the fixed header.
 * @param bottomSlot Optional composable shown below the search field when focused
 *        (e.g. search suggestions or filters).
 */
@Composable
@OptIn(ExperimentalLemonadeComponent::class)
public fun LemonadeUi.TopBar(
    label: String,
    variant: TopBarVariant,
    searchInput: String,
    onSearchChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: TopBarState = rememberTopBarState(),
    navigationAction: TopBarAction? = null,
    onNavigationActionClicked: (() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (BoxScope.() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    var isSearchFocused by remember {
        mutableStateOf(false)
    }
    CoreTopBar(
        state = state,
        variant = variant,
        fixedHeaderSlot = { headerModifier ->
            AnimatedContent(
                modifier = headerModifier
                    .background(color = variant.backgroundColor)
                    .zIndex(zIndex = 1f)
                    .padding(
                        horizontal = LocalSpaces.current.spacing200,
                        vertical = LocalSpaces.current.spacing50,
                    ),
                targetState = isSearchFocused,
                transitionSpec = { expandVertically() togetherWith shrinkVertically() },
                content = { searchFocused ->
                    if (!searchFocused) {
                        CoreTopBarContent(
                            leadingSlot = {
                                if (navigationAction != null && onNavigationActionClicked != null) {
                                    CoreTopBarActionContent(
                                        navigationAction = navigationAction,
                                        onNavigationActionClicked = onNavigationActionClicked,
                                        modifier = Modifier.matchParentSize(),
                                    )
                                }
                            },
                            trailingSlot = trailingSlot,
                            label = label,
                            variant = variant,
                        )
                    }
                }
            )
        },
        collapsableSlot = { collapsableModifier ->
            CoreSearchField(
                input = searchInput,
                onInputChanged = onSearchChanged,
                leadingIcon = if (isSearchFocused) {
                    LemonadeIcons.ArrowLeft
                } else {
                    LemonadeIcons.Search
                },
                onLeadingIconClicked = focusManager::clearFocus,
                modifier = collapsableModifier
                    .padding(horizontal = LocalSpaces.current.spacing400)
                    .padding(
                        top = LocalSpaces.current.spacing50,
                        bottom = LocalSpaces.current.spacing200,
                    )
                    .onFocusChanged { focusState ->
                        isSearchFocused = focusState.isFocused
                        state.setAnimationGesturesLock(locked = focusState.isFocused)
                        if (focusState.isFocused) {
                            state.expand()
                        }
                    }
            )
        },
        bottomSlot = bottomSlot?.let {
            {
                AnimatedContent(
                    targetState = isSearchFocused,
                    transitionSpec = { expandVertically() togetherWith shrinkVertically() },
                    content = { searchFocused ->
                        if (searchFocused) {
                            bottomSlot()
                        }
                    }
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun CoreTopBarActionContent(
    navigationAction: TopBarAction,
    onNavigationActionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> LocalColors.current.interaction.bgNeutralSubtlePressed
            else -> LocalColors.current.interaction.bgNeutralSubtlePressed.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )
        }
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = LocalShapes.current.radius300,
            )
            .clickable(
                role = Role.Button,
                onClick = onNavigationActionClicked,
                interactionSource = interactionSource,
                indication = null,
            )
    ) {
        LemonadeUi.Icon(
            icon = navigationAction.icon,
            contentDescription = navigationAction.name,
            size = LemonadeAssetSize.Medium,
        )
    }
}

@Composable
private fun CoreTopBar(
    state: TopBarState,
    variant: TopBarVariant,
    fixedHeaderSlot: @Composable (modifier: Modifier) -> Unit,
    collapsableSlot: @Composable (modifier: Modifier) -> Unit,
    bottomSlot: @Composable (BoxScope.() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    TopBarLayout(
        state = state,
        modifier = modifier
            .clipToBounds()
            .background(color = variant.backgroundColor),
        fixedHeaderSlot = fixedHeaderSlot,
        collapsableSlot = collapsableSlot,
        bottomStickySlot = bottomSlot?.let { content ->
            { bottomSlotModifier ->
                Box(
                    content = content,
                    modifier = bottomSlotModifier.fillMaxWidth(),
                )
            }
        },
        dividerSlot = { dividerModifier ->
            val dividerAlpha by animateFloatAsState(
                targetValue = if (state.collapseProgress == 1f) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = FastOutSlowInEasing
                ),
                label = "DividerOpacity"
            )

            Spacer(
                modifier = dividerModifier
                    .alpha(alpha = dividerAlpha)
                    .fillMaxWidth()
                    .background(color = LocalColors.current.border.borderNeutralMedium)
                    .height(height = LocalBorderWidths.current.base.border25),
            )
        },
    )
}

private const val LAYOUT_ID_FIXED_HEADER = "fixed_header"
private const val LAYOUT_ID_DIVIDER = "divider"
private const val LAYOUT_ID_COLLAPSABLE_SLOT = "collapsable_slot"
private const val LAYOUT_ID_BOTTOM_SLOT = "bottom_slot"

@Composable
internal fun TopBarLayout(
    state: TopBarState,
    fixedHeaderSlot: @Composable (modifier: Modifier) -> Unit,
    dividerSlot: @Composable (modifier: Modifier) -> Unit,
    collapsableSlot: @Composable (modifier: Modifier) -> Unit,
    bottomStickySlot: (@Composable (modifier: Modifier) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            fixedHeaderSlot(
                Modifier.layoutId(layoutId = LAYOUT_ID_FIXED_HEADER),
            )

            dividerSlot(
                Modifier.layoutId(layoutId = LAYOUT_ID_DIVIDER),
            )

            collapsableSlot(
                Modifier
                    .layoutId(layoutId = LAYOUT_ID_COLLAPSABLE_SLOT)
                    .graphicsLayer {
                        translationY = state.heightOffset
                        alpha = 1f - state.collapseProgress
                    },
            )

            if (bottomStickySlot != null) {
                bottomStickySlot(
                    Modifier.layoutId(layoutId = LAYOUT_ID_BOTTOM_SLOT),
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            val fixedHeaderPlaceable = measurables
                .first { measurable -> measurable.layoutId == LAYOUT_ID_FIXED_HEADER }
                .measure(constraints = constraints)

            val collapsablePlaceable = measurables
                .first { measurable -> measurable.layoutId == LAYOUT_ID_COLLAPSABLE_SLOT }
                .measure(constraints = constraints)

            val bottomSlotPlaceable = measurables
                .find { measurable -> measurable.layoutId == LAYOUT_ID_BOTTOM_SLOT }
                ?.measure(constraints = constraints)

            val dividerPlaceable = measurables
                .first { measurable -> measurable.layoutId == LAYOUT_ID_DIVIDER }
                .measure(constraints = constraints)

            state.maxScrollOffset = collapsablePlaceable.height.toFloat()

            val visibleCollapsablePlaceableHeight =
                (collapsablePlaceable.height + state.heightOffset)
                    .coerceAtLeast(minimumValue = 0f)
                    .roundToInt()

            val totalHeight = fixedHeaderPlaceable.height +
                    dividerPlaceable.height +
                    visibleCollapsablePlaceableHeight +
                    (bottomSlotPlaceable?.height ?: 0)

            layout(
                width = constraints.maxWidth,
                height = totalHeight,
            ) {
                var yPosition = 0

                fixedHeaderPlaceable.placeRelative(
                    x = 0,
                    y = yPosition,
                )
                yPosition += fixedHeaderPlaceable.height


                collapsablePlaceable.placeRelative(
                    x = 0,
                    y = yPosition,
                )
                yPosition += visibleCollapsablePlaceableHeight

                bottomSlotPlaceable?.placeRelative(
                    x = 0,
                    y = yPosition,
                )
                yPosition += bottomSlotPlaceable?.height ?: 0

                dividerPlaceable.placeRelative(
                    x = 0,
                    y = yPosition,
                )
            }
        }
    )
}

@Composable
internal fun CoreTopBarContent(
    leadingSlot: @Composable (BoxScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    label: String,
    variant: TopBarVariant,
    modifier: Modifier = Modifier,
    labelAlpha: Float = LocalOpacities.current.base.opacity100,
) {
    val labelVisible = labelAlpha == 1f
    val animatedLabelOffsetY by animateDpAsState(
        targetValue = if (labelVisible) 0.dp else 8.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "LabelYOffset"
    )

    val animatedLabelAlpha by animateFloatAsState(
        targetValue = if (labelVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "LabelOpacity"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(LocalSizes.current.size1100)
            .padding(
                horizontal = LocalSpaces.current.spacing100,
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.requiredSize(size = LocalSizes.current.size1000),
            content = {
                leadingSlot?.invoke(this)
            },
        )

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.headingXXSmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .weight(weight = 1f)
                .offset(y = animatedLabelOffsetY)
                .alpha(alpha = animatedLabelAlpha),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.requiredHeightIn(max = LocalSizes.current.size1000),
            horizontalArrangement = Arrangement.End,
        ) {
            trailingSlot?.invoke(this)
        }
    }
}

/**
 * State holder for [TopBar][LemonadeUi.TopBar] that manages the collapse/expand
 * behavior based on scroll events from external scrollable content.
 *
 * Use [rememberTopBarState] to create and remember an instance.
 *
 * @param coroutineScope The [CoroutineScope] used to launch scroll-offset animations.
 * @param startCollapsed When `true`, the top bar starts in the collapsed state.
 * @param startWithLockedGestureAnimation When `true`, scroll gestures will not collapse or expand the
 *        top bar; only programmatic calls to [collapse] and [expand] will work.
 * @see rememberTopBarState
 */
@Stable
public class TopBarState internal constructor(
    private val coroutineScope: CoroutineScope,
    private val startCollapsed: Boolean = false,
    private val startWithLockedGestureAnimation: Boolean = false,
) {

    private var lockGestureAnimation by mutableStateOf(startWithLockedGestureAnimation)

    private val scrollOffsetAnimatable by derivedStateOf {
        Animatable(
            initialValue = if (startCollapsed) {
                maxScrollOffset
            } else {
                0f
            },
        )
    }

    internal val scrollOffset: Float
        get() = scrollOffsetAnimatable.value
    internal var maxScrollOffset: Float by mutableFloatStateOf(0f)

    public val collapseProgress: Float by derivedStateOf {
        if (maxScrollOffset > 0f) {
            (scrollOffset / maxScrollOffset).coerceIn(
                minimumValue = 0f,
                maximumValue = 1f,
            )
        } else {
            0f
        }
    }

    internal val heightOffset: Float by derivedStateOf {
        -scrollOffset
    }

    /**
     * Animates the top bar to the fully collapsed state.
     * Does nothing if already fully collapsed.
     *
     * @param animationSpec The animation specification to use. Defaults to a 300ms tween.
     */
    public fun collapse(
        animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
    ) {
        if (scrollOffset < maxScrollOffset) {
            coroutineScope.launch {
                scrollOffsetAnimatable.animateTo(
                    targetValue = maxScrollOffset,
                    animationSpec = animationSpec,
                )
            }
        }
    }

    /**
     * Animates the top bar to the fully expanded state.
     * Does nothing if already fully expanded.
     *
     * @param animationSpec The animation specification to use. Defaults to a 300ms tween.
     */
    public fun expand(
        animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
    ) {
        if (scrollOffset > 0f) {
            coroutineScope.launch {
                scrollOffsetAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = animationSpec,
                )
            }
        }
    }

    public fun setAnimationGesturesLock(locked: Boolean) {
        lockGestureAnimation = locked
    }

    /**
     * [NestedScrollConnection] that captures scroll events from child scrollable content.
     * Apply this to your scrollable content using [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
     *
     * The scroll behavior is:
     * - **Collapse (scroll down)**: Top bar collapses first, then list scrolls
     * - **Expand (scroll up)**: List scrolls to top first, then top bar expands
     *
     * **Note:** When [lockGestureAnimation] is `true`, this connection becomes a no-op—all scroll
     * events pass through to the content unchanged.
     *
     * ## Usage
     * ```kotlin
     * val state = rememberTopBarState()
     *
     * LazyColumn(
     *     modifier = Modifier.nestedScroll(state.nestedScrollConnection)
     * ) {
     *     // content
     * }
     * ```
     */
    public val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (lockGestureAnimation) {
                return Offset.Zero
            }
            val delta = available.y
            if (delta < 0 && scrollOffset < maxScrollOffset) {
                val newOffset = scrollOffset - delta
                val previousOffset = scrollOffset
                val targetOffset = newOffset.coerceIn(
                    minimumValue = 0f,
                    maximumValue = maxScrollOffset,
                )
                coroutineScope.launch {
                    scrollOffsetAnimatable.snapTo(targetValue = targetOffset)
                }
                return Offset(
                    x = 0f,
                    y = previousOffset - targetOffset,
                )
            }
            return Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (lockGestureAnimation) {
                return Offset.Zero
            }
            val delta = available.y
            if (delta > 0 && scrollOffset > 0f) {
                val newOffset = scrollOffset - delta
                val previousOffset = scrollOffset
                val targetOffset = newOffset.coerceIn(
                    minimumValue = 0f,
                    maximumValue = maxScrollOffset,
                )
                coroutineScope.launch {
                    scrollOffsetAnimatable.snapTo(targetValue = targetOffset)
                }
                return Offset(
                    x = 0f,
                    y = previousOffset - targetOffset,
                )
            }
            return Offset.Zero
        }
    }
}

private val TopBarVariant.backgroundColor: Color
    @Composable get() {
        return when (this) {
            TopBarVariant.Default -> LocalColors.current.background.bgDefault
            TopBarVariant.Subtle -> LocalColors.current.background.bgSubtle
        }
    }

private val TopBarAction.icon: LemonadeIcons
    @Composable get() {
        return when (this) {
            TopBarAction.Back -> LemonadeIcons.ArrowLeft
            TopBarAction.Close -> LemonadeIcons.Times
        }
    }

private data class TopBarPreviewData(
    val collapsed: Boolean,
    val action: TopBarAction,
    val variant: TopBarVariant,
)

private class TopBarPreviewProvider : PreviewParameterProvider<TopBarPreviewData> {
    override val values: Sequence<TopBarPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<TopBarPreviewData> {
        return buildList {
            listOf(true, false).forEach { collapsed ->
                TopBarAction.entries.forEach { action ->
                    TopBarVariant.entries.forEach { variant ->
                        add(
                            element = TopBarPreviewData(
                                collapsed = collapsed,
                                action = action,
                                variant = variant,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun TopBarPreview(
    @PreviewParameter(TopBarPreviewProvider::class)
    previewData: TopBarPreviewData,
) {
    LemonadeUi.TopBar(
        label = "Label",
        collapsedLabel = "Collapsed Label",
        variant = previewData.variant,
        navigationAction = previewData.action,
        state = rememberTopBarState(
            startCollapsed = previewData.collapsed,
        ),
    )
}

@LemonadePreview
@Composable
private fun SearchableTopBarPreview(
    @PreviewParameter(TopBarPreviewProvider::class)
    previewData: TopBarPreviewData,
) {
    LemonadeUi.TopBar(
        label = "Label",
        variant = previewData.variant,
        navigationAction = previewData.action,
        searchInput = "Search",
        onSearchChanged = { /* Search Callback */ },
        state = rememberTopBarState(
            startCollapsed = previewData.collapsed,
        )
    )
}
