package com.teya.lemonade

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.TooltipFooterActionVariant
import com.teya.lemonade.core.TooltipIndicatorPlacement
import com.teya.lemonade.core.TooltipScrim
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlin.math.abs
import kotlin.math.roundToInt

/** Gap between the indicator tip and the anchor it points at. */
private val TooltipAnchorGap = 4.dp

/** Smallest distance the tooltip keeps from the edges of the host. */
private val TooltipHostMargin = 8.dp

/** Space left around the anchor when the spotlight scrim punches through. */
private val TooltipSpotlightPadding = 4.dp

/** Corner radius of the spotlight cut-out. */
private val TooltipSpotlightRadius = 12.dp

/** Blur applied to the host content that shows through the tooltip's translucent surface. */
private val TooltipBackdropBlurRadius = 24.dp

/**
 * One step of a guided tour. See [LemonadeTooltipState.startTour].
 *
 * @param anchor Key of the element this step points at, as registered by
 *   [Modifier.lemonadeTooltipAnchor]. A step whose anchor is not currently on screen does not render
 *   until it appears, so a tour can span several screens.
 * @param content The body text of the step.
 * @param title Optional bold heading.
 * @param indicatorPlacement Forces where the indicator sits. Defaults to `null`, which resolves the
 *   placement from where the anchor is on screen.
 * @param cover Optional cover slot, rendered above the text.
 */
public class LemonadeTooltipStep(
    public val anchor: String,
    public val content: String,
    public val title: String? = null,
    public val indicatorPlacement: TooltipIndicatorPlacement? = null,
    public val cover: (@Composable BoxScope.() -> Unit)? = null,
)

/**
 * Text the host puts in a tour's footer. Pass translated strings to localise a tour.
 *
 * @param next Label of the action that advances to the next step.
 * @param done Label that replaces [next] on the final step.
 * @param skip Label of the action that abandons the tour. Pass `null` to leave it out.
 * @param stepSeparator Word between the two numbers of the step counter, as in `1 of 3`.
 */
public class LemonadeTooltipTourLabels(
    public val next: String = "Next",
    public val done: String = "Done",
    public val skip: String? = "Skip",
    public val stepSeparator: String = "of",
)

internal class TooltipPresentation(
    val id: Int,
    val anchor: String,
    val content: String,
    val title: String?,
    val indicatorPlacement: TooltipIndicatorPlacement?,
    val scrim: TooltipScrim,
    val dismissOnOutsideTap: Boolean,
    val onCloseClick: (() -> Unit)?,
    val closeContentDescription: String?,
    val cover: (@Composable BoxScope.() -> Unit)?,
    val footer: (@Composable TooltipFooterScope.() -> Unit)?,
)

private class TooltipTour(
    val steps: List<LemonadeTooltipStep>,
    val labels: LemonadeTooltipTourLabels,
    val scrim: TooltipScrim,
    val onFinish: () -> Unit,
    val onSkip: () -> Unit,
)

/**
 * State holder for the Lemonade Tooltip system. Obtain it via [LocalLemonadeTooltipState].
 *
 * A tooltip always points at an element, so the element has to be tagged with
 * [Modifier.lemonadeTooltipAnchor] before anything can be shown against it. Only one tooltip is
 * visible at a time and showing another replaces it. Nothing is queued: unlike a toast, a tooltip
 * describes something on screen, so a queued one would usually be stale by the time it surfaced.
 */
@Stable
public class LemonadeTooltipState {
    internal var presentation: TooltipPresentation? by mutableStateOf(null)
        private set

    internal var hostBounds: Rect? by mutableStateOf(null)

    private val anchors = mutableStateMapOf<String, Rect>()

    private var tour: TooltipTour? by mutableStateOf(null)

    private var stepIndex: Int by mutableStateOf(0)

    private var nextId: Int = 0

    /** Index of the step being shown, or `-1` when no tour is running. */
    public val currentStepIndex: Int
        get() {
            if (tour == null) {
                return -1
            }
            return stepIndex
        }

    /** Number of steps in the running tour, or `0` when none is running. */
    public val tourStepCount: Int
        get() {
            return tour?.steps?.size
                ?: 0
        }

    /** Whether a tooltip is currently being presented. */
    public val isVisible: Boolean
        get() {
            return presentation != null
        }

    /**
     * Shows a single tooltip against [anchor]. Anything already showing is replaced.
     *
     * ## Usage
     * ```kotlin
     * val tooltips = LocalLemonadeTooltipState.current
     * tooltips.show(
     *     anchor = "fees-info",
     *     content = "Fees are deducted daily.",
     * )
     * ```
     *
     * @param anchor Key of the element to point at, as registered by
     *   [Modifier.lemonadeTooltipAnchor]. Nothing renders until an element with this key is on screen.
     * @param content The body text.
     * @param title Optional bold heading.
     * @param indicatorPlacement Forces where the indicator sits. Defaults to `null`, which resolves
     *   the placement from where the anchor is on screen.
     * @param scrim What to draw behind the tooltip. Defaults to [TooltipScrim.None] — on-demand help
     *   usually should not dim the screen.
     * @param dismissOnOutsideTap Whether a tap outside the tooltip dismisses it. Defaults to `true`.
     *   Either way the host swallows the tap, so the UI underneath is never acted on by accident.
     * @param showCloseButton Whether to show the close button. Defaults to `false`.
     * @param closeContentDescription Accessibility label for the close button.
     * @param cover Optional cover slot, rendered above the text.
     * @param footer Optional footer slot. See [TooltipFooterScope].
     */
    @Suppress("LongParameterList")
    public fun show(
        anchor: String,
        content: String,
        title: String? = null,
        indicatorPlacement: TooltipIndicatorPlacement? = null,
        scrim: TooltipScrim = TooltipScrim.None,
        dismissOnOutsideTap: Boolean = true,
        showCloseButton: Boolean = false,
        closeContentDescription: String? = null,
        cover: (@Composable BoxScope.() -> Unit)? = null,
        footer: (@Composable TooltipFooterScope.() -> Unit)? = null,
    ) {
        tour = null

        val onCloseClick: (() -> Unit)? = if (showCloseButton) {
            { dismiss() }
        } else {
            null
        }

        presentation = TooltipPresentation(
            id = nextId++,
            anchor = anchor,
            content = content,
            title = title,
            indicatorPlacement = indicatorPlacement,
            scrim = scrim,
            dismissOnOutsideTap = dismissOnOutsideTap,
            onCloseClick = onCloseClick,
            closeContentDescription = closeContentDescription,
            cover = cover,
            footer = footer,
        )
    }

    /**
     * Starts a guided tour. The host builds each step's footer itself — the step counter, the skip
     * action and the next/done action — so a caller only describes the steps.
     *
     * ## Usage
     * ```kotlin
     * tooltips.startTour(
     *     steps = listOf(
     *         LemonadeTooltipStep(
     *             anchor = "takings",
     *             title = "Daily takings",
     *             content = "Everything you sold today.",
     *         ),
     *     ),
     *     onFinish = { markOnboardingSeen() },
     * )
     * ```
     *
     * @param steps The steps, in order. An empty list does nothing.
     * @param labels Text for the generated footer. Pass translated strings to localise a tour.
     * @param scrim What to draw behind each step. Defaults to [TooltipScrim.Spotlight], which keeps
     *   the element being described lit while dimming the rest.
     * @param onFinish Invoked once the final step is confirmed.
     * @param onSkip Invoked when the tour is abandoned, whether by the skip action, the close button
     *   or an outside tap.
     */
    public fun startTour(
        steps: List<LemonadeTooltipStep>,
        labels: LemonadeTooltipTourLabels = LemonadeTooltipTourLabels(),
        scrim: TooltipScrim = TooltipScrim.Spotlight,
        onFinish: () -> Unit = {},
        onSkip: () -> Unit = {},
    ) {
        if (steps.isEmpty()) {
            return
        }

        tour = TooltipTour(
            steps = steps,
            labels = labels,
            scrim = scrim,
            onFinish = onFinish,
            onSkip = onSkip,
        )
        stepIndex = 0
        presentCurrentStep()
    }

    /** Advances to the next step, finishing the tour if the current step is the last one. */
    public fun next() {
        val currentTour = tour
            ?: return

        if (stepIndex >= currentTour.steps.lastIndex) {
            tour = null
            presentation = null
            currentTour.onFinish()
            return
        }

        stepIndex += 1
        presentCurrentStep()
    }

    /** Goes back one step. Does nothing on the first step. */
    public fun previous() {
        if (tour == null || stepIndex == 0) {
            return
        }

        stepIndex -= 1
        presentCurrentStep()
    }

    /** Abandons the running tour and invokes its `onSkip`. */
    public fun skip() {
        val currentTour = tour
            ?: return

        tour = null
        presentation = null
        currentTour.onSkip()
    }

    /** Dismisses whatever is showing. A running tour is abandoned, which invokes its `onSkip`. */
    public fun dismiss() {
        if (tour != null) {
            skip()
            return
        }

        presentation = null
    }

    internal fun updateAnchor(
        key: String,
        bounds: Rect,
    ) {
        anchors[key] = bounds
    }

    internal fun removeAnchor(key: String) {
        anchors.remove(key)
    }

    /** Anchor bounds translated into the host's own coordinate space, or `null` if not on screen. */
    internal fun anchorBoundsInHost(key: String): Rect? {
        val host = hostBounds
            ?: return null
        val bounds = anchors[key]
            ?: return null

        return bounds.translate(
            translateX = -host.left,
            translateY = -host.top,
        )
    }

    private fun presentCurrentStep() {
        val currentTour = tour
            ?: return
        val step = currentTour.steps[stepIndex]

        presentation = TooltipPresentation(
            id = nextId++,
            anchor = step.anchor,
            content = step.content,
            title = step.title,
            indicatorPlacement = step.indicatorPlacement,
            scrim = currentTour.scrim,
            dismissOnOutsideTap = true,
            onCloseClick = { skip() },
            closeContentDescription = currentTour.labels.skip,
            cover = step.cover,
            footer = tourFooter(tour = currentTour, index = stepIndex),
        )
    }

    private fun tourFooter(
        tour: TooltipTour,
        index: Int,
    ): @Composable TooltipFooterScope.() -> Unit =
        {
            val isLast = index == tour.steps.lastIndex

            StepCounter(
                currentStep = index + 1,
                totalSteps = tour.steps.size,
                modifier = Modifier.weight(weight = 1f),
                separator = tour.labels.stepSeparator,
            )

            val skipLabel = tour.labels.skip
            if (skipLabel != null && !isLast) {
                Action(
                    label = skipLabel,
                    onClick = { skip() },
                    variant = TooltipFooterActionVariant.Secondary,
                )
            }

            Action(
                label = if (isLast) tour.labels.done else tour.labels.next,
                onClick = { next() },
            )
        }
}

/**
 * Provides the [LemonadeTooltipState] to the composition tree. Must be used inside
 * [LemonadeTooltipHost].
 */
public val LocalLemonadeTooltipState: ProvidableCompositionLocal<LemonadeTooltipState> =
    staticCompositionLocalOf {
        error("No LemonadeTooltipState provided. Wrap your content with LemonadeTooltipHost.")
    }

/**
 * Registers this element as a tooltip anchor under [key], so [LemonadeTooltipState.show] and
 * [LemonadeTooltipState.startTour] can point at it.
 *
 * Keys are global to the enclosing [LemonadeTooltipHost]. The registration is removed when the
 * element leaves composition, so a tooltip aimed at an anchor that is not on screen simply does not
 * render — which is what lets a tour walk across several screens.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Button(
 *     label = "Takings",
 *     onClick = { },
 *     modifier = Modifier.lemonadeTooltipAnchor(key = "takings"),
 * )
 * ```
 */
public fun Modifier.lemonadeTooltipAnchor(key: String): Modifier =
    composed {
        val state = LocalLemonadeTooltipState.current

        DisposableEffect(key) {
            onDispose { state.removeAnchor(key = key) }
        }

        onGloballyPositioned { coordinates ->
            state.updateAnchor(
                key = key,
                bounds = coordinates.boundsInWindow(),
            )
        }
    }

/**
 * Provides [LemonadeTooltipState] to the composition tree and renders the tooltip overlay — the
 * scrim and the anchored tooltip itself — above your content.
 *
 * Place this at the root of your app, wrapping your main content.
 *
 * ## Usage
 * ```kotlin
 * LemonadeTooltipHost {
 *     // Your app content
 * }
 * ```
 */
@Composable
public fun LemonadeTooltipHost(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val tooltipState = remember { LemonadeTooltipState() }

    // Captures the host content so the tooltip can blur what shows through its translucent surface.
    // Compose has no backdrop-blur modifier and a GraphicsLayer cannot be re-drawn by a second
    // owner, so the capture has to come from Haze.
    val hazeState = rememberHazeState()

    CompositionLocalProvider(LocalLemonadeTooltipState provides tooltipState) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                tooltipState.hostBounds = coordinates.boundsInWindow()
            },
        ) {
            Box(modifier = Modifier.hazeSource(state = hazeState)) {
                content()
            }

            val presentation = tooltipState.presentation
            val hostBounds = tooltipState.hostBounds
            val anchor = presentation?.let { current ->
                tooltipState.anchorBoundsInHost(key = current.anchor)
            }

            if (presentation != null && hostBounds != null && anchor != null) {
                TooltipOverlay(
                    state = tooltipState,
                    presentation = presentation,
                    anchor = anchor,
                    hostSize = Size(width = hostBounds.width, height = hostBounds.height),
                    hazeState = hazeState,
                )
            }
        }
    }
}

@Composable
private fun TooltipOverlay(
    state: LemonadeTooltipState,
    presentation: TooltipPresentation,
    anchor: Rect,
    hostSize: Size,
    hazeState: HazeState,
) {
    val colors = LocalColors.current
    val opacities = LocalOpacities.current
    val radius = LocalRadius.current
    val density = LocalDensity.current

    val scrimSource = colors.background.bgAlwaysDark
    val scrimColor = scrimSource.copy(alpha = scrimSource.alpha * opacities.base.opacity40)
    val spotlight = anchor.inflate(delta = with(density) { TooltipSpotlightPadding.toPx() })
    val spotlightRadius = with(density) { TooltipSpotlightRadius.toPx() }

    // The tooltip is composed with its final indicator before it is measured, so the placement is
    // resolved from the anchor and the host alone — neither of which needs the measured size.
    val pointsUp = resolvePointsUp(anchor = anchor, hostSize = hostSize)
    val placement = presentation.indicatorPlacement
        ?: resolveIndicatorPlacement(
            anchor = anchor,
            hostSize = hostSize,
            tooltipWidth = with(density) { TooltipWidth.toPx() },
            margin = with(density) { TooltipHostMargin.toPx() },
            edgeInset = with(density) { TooltipIndicatorEdgeInset.toPx() },
            baseWidth = with(density) { TooltipIndicatorBaseWidth.toPx() },
            pointsUp = pointsUp,
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(presentation.id, presentation.dismissOnOutsideTap) {
                // The tap is always swallowed so the UI underneath cannot be acted on while a
                // tooltip is up; whether it also dismisses is the caller's choice.
                detectTapGestures {
                    if (presentation.dismissOnOutsideTap) {
                        state.dismiss()
                    }
                }
            }.drawBehind {
                when (presentation.scrim) {
                    TooltipScrim.None -> Unit

                    TooltipScrim.Dim -> drawRect(color = scrimColor)

                    TooltipScrim.Spotlight -> {
                        val cutout = Path().apply {
                            addRoundRect(
                                roundRect = RoundRect(
                                    rect = spotlight,
                                    cornerRadius = CornerRadius(spotlightRadius, spotlightRadius),
                                ),
                            )
                        }
                        clipPath(path = cutout, clipOp = ClipOp.Difference) {
                            drawRect(color = scrimColor)
                        }
                    }
                }
            },
    ) {
        val tooltipShape = remember(placement, radius.radius600) {
            TooltipShape(
                indicatorPlacement = placement,
                cornerRadius = radius.radius600,
            )
        }

        Layout(
            content = {
                Box(
                    // Swallow taps on the tooltip itself so they never reach the dismiss handler.
                    modifier = Modifier.pointerInput(presentation.id) {
                        detectTapGestures { }
                    },
                ) {
                    // The tooltip fill is only ~74% opaque, so whatever it covers reads through.
                    // Blurring the backdrop, clipped to the tooltip's own outline, turns busy
                    // content behind it into a wash instead of letting it compete with the text.
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(shape = tooltipShape)
                            .hazeEffect(state = hazeState) {
                                blurRadius = TooltipBackdropBlurRadius
                                // No tint: the tooltip draws its own fill over this.
                                tints = emptyList()
                            },
                    )

                    LemonadeUi.Tooltip(
                        content = presentation.content,
                        title = presentation.title,
                        indicatorPlacement = placement,
                        onCloseClick = presentation.onCloseClick,
                        closeContentDescription = presentation.closeContentDescription,
                        cover = presentation.cover,
                        footer = presentation.footer,
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { measurables, constraints ->
            val placeable = measurables.first().measure(
                constraints = constraints.copy(minWidth = 0, minHeight = 0),
            )
            val offset = resolveTooltipOffset(
                anchor = anchor,
                hostSize = Size(
                    width = constraints.maxWidth.toFloat(),
                    height = constraints.maxHeight.toFloat(),
                ),
                tooltipSize = Size(
                    width = placeable.width.toFloat(),
                    height = placeable.height.toFloat(),
                ),
                placement = placement,
                pointsUp = pointsUp,
                density = this,
            )

            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                placeable.place(position = offset)
            }
        }
    }
}

// MARK: - Placement Resolution

/**
 * Horizontal distance from the tooltip's leading edge to the centre of its indicator. The indicator
 * sits at one of three fixed positions, so this is the reach a given placement can offer.
 */
internal fun indicatorCenterOffset(
    placement: TooltipIndicatorPlacement,
    tooltipWidth: Float,
    edgeInset: Float,
    baseWidth: Float,
): Float =
    when (placement) {
        TooltipIndicatorPlacement.TopLeft,
        TooltipIndicatorPlacement.BottomLeft,
        -> edgeInset + baseWidth / 2f

        TooltipIndicatorPlacement.TopCenter,
        TooltipIndicatorPlacement.BottomCenter,
        TooltipIndicatorPlacement.None,
        -> tooltipWidth / 2f

        TooltipIndicatorPlacement.TopRight,
        TooltipIndicatorPlacement.BottomRight,
        -> tooltipWidth - edgeInset - baseWidth / 2f
    }

/** Whether the tooltip sits below the anchor, with its indicator pointing up at it. */
internal fun resolvePointsUp(
    anchor: Rect,
    hostSize: Size,
): Boolean = anchor.center.y < hostSize.height / 2f

/**
 * Picks the placement whose indicator lands closest to the centre of [anchor] once the tooltip has
 * been kept inside the host. The indicator has only three possible positions, so near an edge the
 * left or right variant reaches an anchor that the centred one cannot.
 */
@Suppress("LongParameterList")
internal fun resolveIndicatorPlacement(
    anchor: Rect,
    hostSize: Size,
    tooltipWidth: Float,
    margin: Float,
    edgeInset: Float,
    baseWidth: Float,
    pointsUp: Boolean,
): TooltipIndicatorPlacement {
    val candidates = if (pointsUp) {
        listOf(
            TooltipIndicatorPlacement.TopCenter,
            TooltipIndicatorPlacement.TopLeft,
            TooltipIndicatorPlacement.TopRight,
        )
    } else {
        listOf(
            TooltipIndicatorPlacement.BottomCenter,
            TooltipIndicatorPlacement.BottomLeft,
            TooltipIndicatorPlacement.BottomRight,
        )
    }

    val anchorCenterX = anchor.center.x
    val maxX = (hostSize.width - tooltipWidth - margin).coerceAtLeast(minimumValue = margin)

    var best = candidates.first()
    var bestError = Float.MAX_VALUE
    candidates.forEach { candidate ->
        val offset = indicatorCenterOffset(
            placement = candidate,
            tooltipWidth = tooltipWidth,
            edgeInset = edgeInset,
            baseWidth = baseWidth,
        )
        val x = (anchorCenterX - offset).coerceIn(minimumValue = margin, maximumValue = maxX)
        val error = abs(x + offset - anchorCenterX)
        if (error < bestError) {
            bestError = error
            best = candidate
        }
    }

    return best
}

@Suppress("LongParameterList")
internal fun resolveTooltipOffset(
    anchor: Rect,
    hostSize: Size,
    tooltipSize: Size,
    placement: TooltipIndicatorPlacement,
    pointsUp: Boolean,
    density: Density,
): IntOffset {
    val margin = with(density) { TooltipHostMargin.toPx() }
    val gap = with(density) { TooltipAnchorGap.toPx() }
    val edgeInset = with(density) { TooltipIndicatorEdgeInset.toPx() }
    val baseWidth = with(density) { TooltipIndicatorBaseWidth.toPx() }

    val indicatorOffset = indicatorCenterOffset(
        placement = placement,
        tooltipWidth = tooltipSize.width,
        edgeInset = edgeInset,
        baseWidth = baseWidth,
    )

    val maxX = (hostSize.width - tooltipSize.width - margin).coerceAtLeast(minimumValue = margin)
    val x = (anchor.center.x - indicatorOffset)
        .coerceIn(minimumValue = margin, maximumValue = maxX)

    val maxY = (hostSize.height - tooltipSize.height - margin).coerceAtLeast(minimumValue = margin)
    val rawY = if (pointsUp) {
        anchor.bottom + gap
    } else {
        anchor.top - gap - tooltipSize.height
    }
    val y = rawY.coerceIn(minimumValue = margin, maximumValue = maxY)

    return IntOffset(x = x.roundToInt(), y = y.roundToInt())
}
