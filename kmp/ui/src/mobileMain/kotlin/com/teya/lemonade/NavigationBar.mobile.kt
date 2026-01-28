package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.NavigationBarAction
import com.teya.lemonade.core.NavigationBarVariant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * State holder for [NavigationBar][LemonadeUi.NavigationBar] that manages the collapse/expand
 * behavior based on scroll events from external scrollable content.
 *
 * Use [rememberNavigationBarState] to create and remember an instance.
 *
 * @see rememberNavigationBarState
 */
@Stable
public class NavigationBarState internal constructor() {

    private val scrollOffsetAnimatable = Animatable(initialValue = 0f)

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
     * Animates the navigation bar to the fully collapsed state.
     * Does nothing if already fully collapsed.
     *
     * @param animationSpec The animation specification to use. Defaults to a 300ms tween.
     */
    public suspend fun collapse(
        animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
    ) {
        if (scrollOffset < maxScrollOffset) {
            scrollOffsetAnimatable.animateTo(
                targetValue = maxScrollOffset,
                animationSpec = animationSpec,
            )
        }
    }

    /**
     * Animates the navigation bar to the fully expanded state.
     * Does nothing if already fully expanded.
     *
     * @param animationSpec The animation specification to use. Defaults to a 300ms tween.
     */
    public suspend fun expand(
        animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
    ) {
        if (scrollOffset > 0f) {
            scrollOffsetAnimatable.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec,
            )
        }
    }

    /**
     * [NestedScrollConnection] that captures scroll events from child scrollable content.
     * Apply this to your scrollable content using [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
     *
     * The scroll behavior is:
     * - **Collapse (scroll down)**: Navigation bar collapses first, then list scrolls
     * - **Expand (scroll up)**: List scrolls to top first, then navigation bar expands
     *
     * ## Usage
     * ```kotlin
     * val state = rememberNavigationBarState()
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

/**
 * Creates and remembers a [NavigationBarState] instance.
 *
 * @return A remembered [NavigationBarState] instance.
 *
 * ## Usage
 * ```kotlin
 * val navigationBarState = rememberNavigationBarState()
 *
 * Column {
 *     LemonadeUi.NavigationBar(
 *         label = "Screen Title",
 *         state = navigationBarState,
 *         variant = NavigationBarVariant.Default,
 *     )
 *
 *     LazyColumn(
 *         modifier = Modifier.nestedScroll(navigationBarState.nestedScrollConnection)
 *     ) {
 *         // Your content here
 *     }
 * }
 * ```
 */

@Composable
public fun rememberNavigationBarState(): NavigationBarState {
    return remember { NavigationBarState() }
}

/**
 * A collapsible navigation bar component that displays a large title which collapses
 * into a smaller inline title as the user scrolls through content.
 *
 * The NavigationBar works with external scrollable content through [NestedScrollConnection].
 * Use [rememberNavigationBarState] to create the state and apply its [NavigationBarState.nestedScrollConnection]
 * [NavigationBarState.nestedScrollConnection] to your scrollable content via
 * [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
 *
 * ## Usage
 * ```kotlin
 * val navigationBarState = rememberNavigationBarState()
 *
 * Column {
 *     LemonadeUi.NavigationBar(
 *         label = "Screen Title",
 *         state = navigationBarState,
 *         variant = NavigationBarVariant.Default,
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
 *         modifier = Modifier.nestedScroll(navigationBarState.nestedScrollConnection)
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
 * @param state The [NavigationBarState] that manages collapse behavior. Create with [rememberNavigationBarState].
 * @param variant Visual variant of the navigation bar. See [NavigationBarVariant].
 * @param navigationAction Visual variant of the navigation bar's action. See [NavigationBarAction].
 * @param onNavigationActionClicked Callback triggered when the [navigationAction] visual representation is clicked.
 * @param modifier [Modifier] applied to the navigation bar container.
 * @param trailingSlot Optional composable displayed at the end of the fixed header (typically action buttons).
 * @param bottomSlot Optional composable displayed below the expanded title. This content remains
 *        visible and acts as a sticky area when fully collapsed.
 */
@Composable
public fun LemonadeUi.NavigationBar(
    label: String,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
    state: NavigationBarState = rememberNavigationBarState(),
    collapsedLabel: String? = null,
    navigationAction: NavigationBarAction? = null,
    onNavigationActionClicked: (() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (BoxScope.() -> Unit)? = null,
) {
    CoreNavigationBar(
        label = label,
        collapsedLabel = collapsedLabel,
        state = state,
        variant = variant,
        modifier = modifier,
        trailingSlot = trailingSlot,
        bottomSlot = bottomSlot,
        leadingSlot = {
            if (navigationAction != null && onNavigationActionClicked != null) {
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
                    modifier = Modifier
                        .matchParentSize()
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

@Composable
private fun CoreNavigationBar(
    label: String,
    collapsedLabel: String?,
    state: NavigationBarState,
    variant: NavigationBarVariant,
    leadingSlot: @Composable (BoxScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    collapsableSlot: @Composable (modifier: Modifier) -> Unit,
    bottomSlot: @Composable (BoxScope.() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    NavigationBarLayout(
        state = state,
        modifier = modifier.background(color = variant.backgroundColor),
        fixedHeaderSlot = { headerModifier ->
            CoreNavigationBarContent(
                leadingSlot = leadingSlot,
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
            Spacer(
                modifier = dividerModifier
                    .alpha(alpha = state.collapseProgress)
                    .fillMaxWidth()
                    .background(color = LocalColors.current.border.borderNeutralMedium)
                    .height(height = LocalBorderWidths.current.base.border25),
            )
        },
    )
}

private const val LAYOUT_ID_FIXED_HEADER = "fixed_header"
private const val LAYOUT_ID_DIVIDER = "divider"
private const val LAYOUT_ID_EXPANDED_TITLE = "expanded_title"
private const val LAYOUT_ID_BOTTOM_SLOT = "bottom_slot"

@Composable
internal fun NavigationBarLayout(
    state: NavigationBarState,
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
                    .layoutId(layoutId = LAYOUT_ID_EXPANDED_TITLE)
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
                .first { measurable -> measurable.layoutId == LAYOUT_ID_EXPANDED_TITLE }
                .measure(constraints = constraints)

            val bottomSlotPlaceable = measurables
                .find { measurable -> measurable.layoutId == LAYOUT_ID_BOTTOM_SLOT }
                ?.measure(constraints = constraints)

            val dividerPlaceable = measurables
                .first { measurable -> measurable.layoutId == LAYOUT_ID_DIVIDER }
                .measure(constraints = constraints)

            state.maxScrollOffset = collapsablePlaceable.height.toFloat()

            val visibleExpandedTitleHeight = (collapsablePlaceable.height + state.heightOffset)
                .coerceAtLeast(minimumValue = 0f)
                .roundToInt()

            val totalHeight = fixedHeaderPlaceable.height +
                    dividerPlaceable.height +
                    visibleExpandedTitleHeight +
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
                yPosition += visibleExpandedTitleHeight

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
internal fun CoreNavigationBarContent(
    leadingSlot: @Composable (BoxScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    label: String,
    labelAlpha: Float,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
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
                .alpha(alpha = labelAlpha),
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

private val NavigationBarVariant.backgroundColor: Color
    @Composable get() {
        return when (this) {
            NavigationBarVariant.Default -> LocalColors.current.background.bgDefault
            NavigationBarVariant.Subtle -> LocalColors.current.background.bgSubtle
        }
    }

private val NavigationBarAction.icon: LemonadeIcons
    @Composable get() {
        return when (this) {
            NavigationBarAction.Back -> LemonadeIcons.ArrowLeft
            NavigationBarAction.Close -> LemonadeIcons.Times
        }
    }