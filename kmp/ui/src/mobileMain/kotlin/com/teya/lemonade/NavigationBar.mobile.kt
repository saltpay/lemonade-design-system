package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

public enum class NavigationBarVariant {
    Default,
    Subtle,
}

private val NavigationBarVariant.backgroundColor: Color
    @Composable get() {
        return when (this) {
            NavigationBarVariant.Default -> LocalColors.current.background.bgDefault
            NavigationBarVariant.Subtle -> LocalColors.current.background.bgSubtle
        }
    }

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
    /**
     * The current scroll offset in pixels. Ranges from 0 (fully expanded)
     * to [maxScrollOffset] (fully collapsed).
     */
    internal var scrollOffset: Float by mutableFloatStateOf(0f)
        private set

    /**
     * Maximum scroll offset in pixels before the title is fully collapsed.
     * Set internally after layout measurement.
     */
    internal var maxScrollOffset: Float by mutableFloatStateOf(0f)

    /**
     * Progress of the collapse animation, ranging from 0f (fully expanded)
     * to 1f (fully collapsed).
     *
     * Use this value to animate UI elements during the collapse transition.
     */
    public val collapseProgress: Float by derivedStateOf {
        if (maxScrollOffset > 0f) {
            (scrollOffset / maxScrollOffset).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    /**
     * The height reduction to apply to the expanded title area based on scroll.
     */
    internal val heightOffset: Float by derivedStateOf {
        -scrollOffset
    }

    /**
     * [NestedScrollConnection] that captures scroll events from child scrollable content.
     * Apply this to your scrollable content using [Modifier.nestedScroll][androidx.compose.ui.input.nestedscroll.nestedScroll].
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
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            val newOffset = scrollOffset - delta
            val previousOffset = scrollOffset
            scrollOffset = newOffset.coerceIn(0f, maxScrollOffset)
            val consumed = previousOffset - scrollOffset
            return Offset(0f, consumed)
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
 * Use [rememberNavigationBarState] to create the state and apply its [nestedScrollConnection]
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
 * @param label The title text displayed in both expanded (large) and collapsed (small) states.
 * @param state The [NavigationBarState] that manages collapse behavior. Create with [rememberNavigationBarState].
 * @param variant Visual variant of the navigation bar. See [NavigationBarVariant].
 * @param modifier [Modifier] applied to the navigation bar container.
 * @param leadingSlot Optional composable displayed at the start of the fixed header (typically a back button).
 * @param trailingSlot Optional composable displayed at the end of the fixed header (typically action buttons).
 * @param bottomSlot Optional composable displayed below the expanded title. This content remains
 *        visible and acts as a sticky area when fully collapsed.
 */
@Composable
public fun LemonadeUi.NavigationBar(
    label: String,
    state: NavigationBarState,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
    leadingSlot: @Composable (BoxScope.() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (BoxScope.() -> Unit)? = null,
) {
    NavigationBarLayout(
        state = state,
        modifier = modifier.background(color = variant.backgroundColor),
        fixedHeader = { headerModifier ->
            CoreNavigationBarContent(
                leadingSlot = leadingSlot,
                trailingSlot = trailingSlot,
                label = label,
                labelAlpha = state.collapseProgress,
                variant = variant,
                modifier = headerModifier
                    .background(color = variant.backgroundColor)
                    .zIndex(zIndex = 1f)
                    .padding(horizontal = LocalSpaces.current.spacing300)
                    .padding(bottom = LocalSpaces.current.spacing200),
            )
        },
        divider = { dividerModifier ->
            Spacer(
                modifier = dividerModifier
                    .alpha(alpha = state.collapseProgress)
                    .fillMaxWidth()
                    .background(color = LocalColors.current.border.borderNeutralMedium)
                    .height(height = LocalBorderWidths.current.base.border25),
            )
        },
        expandedTitle = { expandedModifier ->
            Box(
                modifier = expandedModifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpaces.current.spacing300)
                    .padding(bottom = LocalSpaces.current.spacing200),
            ) {
                LemonadeUi.Text(
                    text = label,
                    textStyle = LocalTypographies.current.headingLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            }
        },
        bottomSlot = bottomSlot?.let { content ->
            { bottomSlotModifier ->
                Box(
                    content = content,
                    modifier = bottomSlotModifier.fillMaxWidth(),
                )
            }
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
    fixedHeader: @Composable (modifier: Modifier) -> Unit,
    divider: @Composable (modifier: Modifier) -> Unit,
    expandedTitle: @Composable (modifier: Modifier) -> Unit,
    bottomSlot: (@Composable (modifier: Modifier) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            fixedHeader(
                Modifier.layoutId(layoutId = LAYOUT_ID_FIXED_HEADER),
            )

            divider(
                Modifier.layoutId(layoutId = LAYOUT_ID_DIVIDER),
            )

            expandedTitle(
                Modifier
                    .layoutId(LAYOUT_ID_EXPANDED_TITLE)
                    .graphicsLayer {
                        translationY = state.heightOffset
                        alpha = 1f - state.collapseProgress
                    },
            )

            if (bottomSlot != null) {
                bottomSlot(
                    Modifier.layoutId(layoutId = LAYOUT_ID_BOTTOM_SLOT),
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            val fixedHeaderPlaceable = measurables
                .first { it.layoutId == LAYOUT_ID_FIXED_HEADER }
                .measure(constraints)

            val dividerPlaceable = measurables
                .first { it.layoutId == LAYOUT_ID_DIVIDER }
                .measure(constraints)

            val expandedTitlePlaceable = measurables
                .first { it.layoutId == LAYOUT_ID_EXPANDED_TITLE }
                .measure(constraints)

            val bottomSlotPlaceable = measurables
                .find { it.layoutId == LAYOUT_ID_BOTTOM_SLOT }
                ?.measure(constraints)

            state.maxScrollOffset = expandedTitlePlaceable.height.toFloat()

            val visibleExpandedTitleHeight = (expandedTitlePlaceable.height + state.heightOffset)
                .coerceAtLeast(0f)
                .roundToInt()

            val totalHeight = fixedHeaderPlaceable.height +
                    dividerPlaceable.height +
                    visibleExpandedTitleHeight +
                    (bottomSlotPlaceable?.height ?: 0)

            layout(constraints.maxWidth, totalHeight) {
                var yPosition = 0

                fixedHeaderPlaceable.placeRelative(0, yPosition)
                yPosition += fixedHeaderPlaceable.height

                dividerPlaceable.placeRelative(0, yPosition)
                yPosition += dividerPlaceable.height

                expandedTitlePlaceable.placeRelative(0, yPosition)
                yPosition += visibleExpandedTitleHeight

                bottomSlotPlaceable?.placeRelative(0, yPosition)
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
            modifier = Modifier.weight(weight = 1f),
            content = {
                leadingSlot?.invoke(this)
            },
        )

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.headingXXSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.alpha(alpha = labelAlpha),
        )

        Row(
            modifier = Modifier.weight(weight = 1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = LocalSpaces.current.spacing200,
                alignment = Alignment.End,
            ),
        ) {
            trailingSlot?.invoke(this)
        }
    }
}