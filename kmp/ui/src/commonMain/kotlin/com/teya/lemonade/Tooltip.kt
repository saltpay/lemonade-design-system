package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.TooltipFooterActionVariant
import com.teya.lemonade.core.TooltipIndicatorPlacement

internal val TooltipWidth = 280.dp

/** Height of the visible, rounded-off indicator — how far it protrudes past the tooltip body. */
internal val TooltipIndicatorHeight = 8.dp

/** Width of the indicator where it meets the tooltip body. */
internal val TooltipIndicatorBaseWidth = 15.dp

/** Height the indicator would reach if its tip were a sharp point instead of a rounded one. */
internal val TooltipIndicatorApexHeight = 10.dp

/** Distance from the tooltip edge to the near side of the indicator base, for the left/right placements. */
internal val TooltipIndicatorEdgeInset = 40.dp

/**
 * Fraction of the way from the indicator base to its notional apex at which the rounded tip starts.
 * Together with [TOOLTIP_INDICATOR_TIP_CURVATURE] this yields the 3dp tip radius from the design.
 */
private const val TOOLTIP_INDICATOR_TIP_START = 0.68f

/** How far each tip control point is pulled towards the notional apex. */
private const val TOOLTIP_INDICATOR_TIP_CURVATURE = 0.5f

/** Cover slot aspect ratio — 272x158, the cover size at the default 280dp tooltip width. */
private const val TOOLTIP_COVER_ASPECT_RATIO = 272f / 158f

private const val TOOLTIP_STEP_COUNTER_SEPARATOR = "of"

/**
 * Receiver scope for the tooltip footer slot.
 *
 * The footer is a slot, but only a fixed set of parts belongs in it — they are members of this scope
 * rather than standalone components, so they can only be used where they make sense. The scope also
 * exposes [RowScope], so the parts can be spaced and weighted like any other row content.
 */
public class TooltipFooterScope internal constructor(
    private val rowScope: RowScope,
) : RowScope by rowScope {
    /**
     * Progress indicator for a multi-step tooltip tour, rendered as `1 of 3`.
     *
     * Give it `Modifier.weight(1f)` to push the actions to the trailing edge.
     *
     * @param currentStep The step being shown, 1-based.
     * @param totalSteps The total number of steps in the tour.
     * @param modifier Modifier to apply to the counter.
     * @param separator Word placed between the two numbers. Defaults to `"of"` — pass a translated
     *   string to localise it.
     */
    @Composable
    public fun StepCounter(
        currentStep: Int,
        totalSteps: Int,
        modifier: Modifier = Modifier,
        separator: String = TOOLTIP_STEP_COUNTER_SEPARATOR,
    ) {
        val colors = LocalColors.current
        val opacities = LocalOpacities.current

        LemonadeUi.Text(
            text = "$currentStep $separator $totalSteps",
            textStyle = LocalTypographies.current.bodyXSmallMedium,
            color = colors.content.contentAlwaysLight.copy(alpha = opacities.base.opacity80),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier,
        )
    }

    /**
     * A tappable action in the tooltip footer.
     *
     * @param label The text shown on the action.
     * @param onClick Invoked when the action is tapped.
     * @param modifier Modifier to apply to the action.
     * @param variant Emphasis of the action. Defaults to [TooltipFooterActionVariant.Primary].
     */
    @Composable
    public fun Action(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        variant: TooltipFooterActionVariant = TooltipFooterActionVariant.Primary,
    ) {
        val colors = LocalColors.current
        val spaces = LocalSpaces.current
        val shapes = LocalShapes.current
        val sizes = LocalSizes.current
        val typographies = LocalTypographies.current

        val backgroundColor = when (variant) {
            TooltipFooterActionVariant.Primary -> colors.background.bgAlwaysLight
            TooltipFooterActionVariant.Secondary -> Color.Transparent
        }
        val contentColor = when (variant) {
            TooltipFooterActionVariant.Primary -> colors.content.contentAlwaysDark
            TooltipFooterActionVariant.Secondary -> colors.content.contentAlwaysLight
        }
        val textStyle = when (variant) {
            TooltipFooterActionVariant.Primary -> typographies.bodySmallSemiBold
            TooltipFooterActionVariant.Secondary -> typographies.bodySmallMedium
        }
        val borderModifier = when (variant) {
            TooltipFooterActionVariant.Primary -> Modifier
            TooltipFooterActionVariant.Secondary -> Modifier.border(
                width = LocalBorderWidths.current.base.border25,
                color = colors.border.borderNeutralLowInverse,
                shape = shapes.radiusFull,
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .heightIn(min = sizes.size800)
                .clip(shape = shapes.radiusFull)
                .background(color = backgroundColor)
                .then(other = borderModifier)
                .clickable(
                    onClick = onClick,
                    role = Role.Button,
                ).padding(
                    horizontal = spaces.spacing300,
                    vertical = spaces.spacing100,
                ),
        ) {
            LemonadeUi.Text(
                text = label,
                textStyle = textStyle,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * A floating container that explains or draws attention to another element on screen.
 *
 * The tooltip renders its own surface, indicator and content — it does not position itself. Place it
 * in a popup, overlay or anchored layout of your choosing and pick the [indicatorPlacement] that
 * points back at the element being described.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tooltip(
 *     content = "Tap here to see everything you sold today.",
 *     title = "Daily takings",
 *     indicatorPlacement = TooltipIndicatorPlacement.TopLeft,
 *     onCloseClick = { visible = false },
 *     footer = {
 *         StepCounter(
 *             currentStep = 1,
 *             totalSteps = 3,
 *             modifier = Modifier.weight(weight = 1f),
 *         )
 *         Action(
 *             label = "Skip",
 *             onClick = { visible = false },
 *             variant = TooltipFooterActionVariant.Secondary,
 *         )
 *         Action(
 *             label = "Next",
 *             onClick = { step++ },
 *         )
 *     },
 * )
 * ```
 *
 * ## Design Notes
 * The tooltip is 280dp wide by default; pass a width in [modifier] to override it. The indicator is
 * drawn outside the body, so the composable is [TooltipIndicatorHeight] taller than the body itself
 * whenever [indicatorPlacement] is not [TooltipIndicatorPlacement.None].
 *
 * @param content The body text. Required — a tooltip always says something.
 * @param modifier Modifier to apply to the tooltip container.
 * @param title Optional bold heading shown above [content].
 * @param indicatorPlacement Where the indicator points from. Defaults to
 *   [TooltipIndicatorPlacement.None], which draws no indicator.
 * @param onCloseClick Invoked when the close button is tapped. The close button is only shown when
 *   this is non-null.
 * @param closeContentDescription Accessibility label for the close button.
 * @param cover Optional slot rendered above the text, in a 272:158 box clipped to the tooltip's top
 *   corners. Use it for an illustration or screenshot.
 * @param footer Optional slot rendered below the text. See [TooltipFooterScope] for the parts that
 *   belong in it.
 */
@Composable
public fun LemonadeUi.Tooltip(
    content: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    indicatorPlacement: TooltipIndicatorPlacement = TooltipIndicatorPlacement.None,
    onCloseClick: (() -> Unit)? = null,
    closeContentDescription: String? = null,
    cover: (@Composable BoxScope.() -> Unit)? = null,
    footer: (@Composable TooltipFooterScope.() -> Unit)? = null,
) {
    CoreTooltip(
        content = content,
        title = title,
        indicatorPlacement = indicatorPlacement,
        onCloseClick = onCloseClick,
        closeContentDescription = closeContentDescription,
        cover = cover,
        footer = footer,
        modifier = modifier,
    )
}

@Suppress("LongParameterList")
@Composable
private fun CoreTooltip(
    content: String,
    title: String?,
    indicatorPlacement: TooltipIndicatorPlacement,
    onCloseClick: (() -> Unit)?,
    closeContentDescription: String?,
    cover: (@Composable BoxScope.() -> Unit)?,
    footer: (@Composable TooltipFooterScope.() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColors.current
    val spaces = LocalSpaces.current
    val radius = LocalRadius.current
    val opacities = LocalOpacities.current

    val cornerRadius = radius.radius600
    val shape = remember(indicatorPlacement, cornerRadius) {
        TooltipShape(
            indicatorPlacement = indicatorPlacement,
            cornerRadius = cornerRadius,
        )
    }

    // The indicator is drawn outside the body, so the body has to be inset by its height on
    // whichever edge the indicator sits on.
    val indicatorTopInset = if (indicatorPlacement.pointsUp) TooltipIndicatorHeight else 0.dp
    val indicatorBottomInset = if (indicatorPlacement.pointsDown) TooltipIndicatorHeight else 0.dp

    val surfaceColor = colors.background.bgAlwaysDark
    Box(
        modifier = modifier
            .width(width = TooltipWidth)
            .lemonadeShadow(
                shadow = LemonadeShadow.Xlarge,
                shape = shape,
            ).background(
                color = surfaceColor.copy(alpha = surfaceColor.alpha * opacities.base.opacity80),
                shape = shape,
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = indicatorTopInset,
                    bottom = indicatorBottomInset,
                ).padding(all = spaces.spacing100),
        ) {
            if (cover != null) {
                TooltipCover(
                    cornerRadius = cornerRadius,
                    outerPadding = spaces.spacing100,
                    cover = cover,
                )
            }

            TooltipBody(
                content = content,
                title = title,
                footer = footer,
            )
        }

        if (onCloseClick != null) {
            TooltipCloseButton(
                onClick = onCloseClick,
                contentDescription = closeContentDescription,
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(
                        top = indicatorTopInset + spaces.spacing100,
                        end = spaces.spacing100,
                    ),
            )
        }
    }
}

@Composable
private fun TooltipCover(
    cornerRadius: Dp,
    outerPadding: Dp,
    cover: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalColors.current
    val radius = LocalRadius.current

    // The cover sits inside the tooltip's outer padding, so its top corners are the tooltip's own
    // radius less that padding; the bottom corners are the smaller radius the design specifies.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = TOOLTIP_COVER_ASPECT_RATIO)
            .clip(
                shape = RoundedCornerShape(
                    topStart = cornerRadius - outerPadding,
                    topEnd = cornerRadius - outerPadding,
                    bottomStart = radius.radius250,
                    bottomEnd = radius.radius250,
                ),
            ).background(color = colors.background.bgElevatedHigh),
        content = cover,
    )
}

@Composable
private fun TooltipBody(
    content: String,
    title: String?,
    footer: (@Composable TooltipFooterScope.() -> Unit)?,
) {
    val colors = LocalColors.current
    val spaces = LocalSpaces.current
    val opacities = LocalOpacities.current
    val typographies = LocalTypographies.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = spaces.spacing300),
        verticalArrangement = Arrangement.spacedBy(space = spaces.spacing300),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (title != null) {
                LemonadeUi.Text(
                    text = title,
                    textStyle = typographies.bodySmallSemiBold,
                    color = colors.content.contentAlwaysLight,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            LemonadeUi.Text(
                text = content,
                textStyle = typographies.bodySmallRegular,
                color = colors.content.contentAlwaysLight.copy(alpha = opacities.base.opacity90),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (footer != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = spaces.spacing100),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val footerScope = remember(this@Row) { TooltipFooterScope(rowScope = this@Row) }
                footerScope.footer()
            }
        }
    }
}

@Composable
private fun TooltipCloseButton(
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColors.current
    val sizes = LocalSizes.current
    val shapes = LocalShapes.current
    val opacities = LocalOpacities.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = sizes.size800)
            .clip(shape = shapes.radiusFull)
            .clickable(
                onClick = onClick,
                role = Role.Button,
            ).alpha(alpha = opacities.base.opacity60),
    ) {
        LemonadeUi.Icon(
            icon = LemonadeIcons.Times,
            contentDescription = contentDescription,
            size = LemonadeAssetSize.Small,
            tint = colors.content.contentPrimaryInverse,
        )
    }
}

// MARK: - Indicator Geometry

internal val TooltipIndicatorPlacement.pointsUp: Boolean
    get() {
        return when (this) {
            TooltipIndicatorPlacement.TopLeft,
            TooltipIndicatorPlacement.TopCenter,
            TooltipIndicatorPlacement.TopRight,
            -> true

            TooltipIndicatorPlacement.None,
            TooltipIndicatorPlacement.BottomLeft,
            TooltipIndicatorPlacement.BottomCenter,
            TooltipIndicatorPlacement.BottomRight,
            -> false
        }
    }

internal val TooltipIndicatorPlacement.pointsDown: Boolean
    get() {
        return when (this) {
            TooltipIndicatorPlacement.BottomLeft,
            TooltipIndicatorPlacement.BottomCenter,
            TooltipIndicatorPlacement.BottomRight,
            -> true

            TooltipIndicatorPlacement.None,
            TooltipIndicatorPlacement.TopLeft,
            TooltipIndicatorPlacement.TopCenter,
            TooltipIndicatorPlacement.TopRight,
            -> false
        }
    }

/**
 * Rounded-rectangle surface with the tooltip indicator carved into one edge.
 *
 * The indicator is drawn outside the body rectangle, so the shape expects to be laid out over a size
 * that already includes [TooltipIndicatorHeight] on the indicator's edge.
 */
private data class TooltipShape(
    private val indicatorPlacement: TooltipIndicatorPlacement,
    private val cornerRadius: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline =
        Outline.Generic(
            path = buildPath(
                size = size,
                density = density,
            ),
        )

    private fun buildPath(
        size: Size,
        density: Density,
    ): Path {
        val indicatorHeight = with(density) { TooltipIndicatorHeight.toPx() }
        val baseWidth = with(density) { TooltipIndicatorBaseWidth.toPx() }
        val apexHeight = with(density) { TooltipIndicatorApexHeight.toPx() }
        val edgeInset = with(density) { TooltipIndicatorEdgeInset.toPx() }

        val bodyTop = if (indicatorPlacement.pointsUp) indicatorHeight else 0f
        val bodyBottom = if (indicatorPlacement.pointsDown) size.height - indicatorHeight else size.height
        val bodyHeight = bodyBottom - bodyTop
        val radius = with(density) { cornerRadius.toPx() }
            .coerceAtMost(maximumValue = minOf(size.width, bodyHeight) / 2f)
        val diameter = radius * 2f

        val centerX = indicatorCenterX(
            width = size.width,
            baseWidth = baseWidth,
            edgeInset = edgeInset,
        )

        return Path().apply {
            moveTo(radius, bodyTop)
            if (indicatorPlacement.pointsUp) {
                addIndicator(
                    centerX = centerX,
                    baseY = bodyTop,
                    baseWidth = baseWidth,
                    apexHeight = apexHeight,
                    pointsUp = true,
                )
            }
            lineTo(size.width - radius, bodyTop)
            arcTo(
                rect = Rect(size.width - diameter, bodyTop, size.width, bodyTop + diameter),
                startAngleDegrees = -QUARTER_TURN,
                sweepAngleDegrees = QUARTER_TURN,
                forceMoveTo = false,
            )
            lineTo(size.width, bodyBottom - radius)
            arcTo(
                rect = Rect(size.width - diameter, bodyBottom - diameter, size.width, bodyBottom),
                startAngleDegrees = 0f,
                sweepAngleDegrees = QUARTER_TURN,
                forceMoveTo = false,
            )
            if (indicatorPlacement.pointsDown) {
                addIndicator(
                    centerX = centerX,
                    baseY = bodyBottom,
                    baseWidth = baseWidth,
                    apexHeight = apexHeight,
                    pointsUp = false,
                )
            }
            lineTo(radius, bodyBottom)
            arcTo(
                rect = Rect(0f, bodyBottom - diameter, diameter, bodyBottom),
                startAngleDegrees = QUARTER_TURN,
                sweepAngleDegrees = QUARTER_TURN,
                forceMoveTo = false,
            )
            lineTo(0f, bodyTop + radius)
            arcTo(
                rect = Rect(0f, bodyTop, diameter, bodyTop + diameter),
                startAngleDegrees = HALF_TURN,
                sweepAngleDegrees = QUARTER_TURN,
                forceMoveTo = false,
            )
            close()
        }
    }

    private fun indicatorCenterX(
        width: Float,
        baseWidth: Float,
        edgeInset: Float,
    ): Float =
        when (indicatorPlacement) {
            TooltipIndicatorPlacement.TopLeft,
            TooltipIndicatorPlacement.BottomLeft,
            -> edgeInset + baseWidth / 2f

            TooltipIndicatorPlacement.TopCenter,
            TooltipIndicatorPlacement.BottomCenter,
            TooltipIndicatorPlacement.None,
            -> width / 2f

            TooltipIndicatorPlacement.TopRight,
            TooltipIndicatorPlacement.BottomRight,
            -> width - edgeInset - baseWidth / 2f
        }

    /**
     * Appends the indicator to the edge currently being traced. The outline is traced clockwise, so
     * the top edge runs left-to-right and the bottom edge right-to-left — [pointsUp] picks which.
     */
    private fun Path.addIndicator(
        centerX: Float,
        baseY: Float,
        baseWidth: Float,
        apexHeight: Float,
        pointsUp: Boolean,
    ) {
        val halfBase = baseWidth / 2f
        val apexY = if (pointsUp) baseY - apexHeight else baseY + apexHeight
        val entryX = if (pointsUp) centerX - halfBase else centerX + halfBase
        val exitX = if (pointsUp) centerX + halfBase else centerX - halfBase

        // The tip is rounded off: each straight edge stops short of the notional apex, and a cubic
        // whose control points lean towards that apex bridges the gap.
        val tipEntryX = entryX + (centerX - entryX) * TOOLTIP_INDICATOR_TIP_START
        val tipExitX = exitX + (centerX - exitX) * TOOLTIP_INDICATOR_TIP_START
        val tipY = baseY + (apexY - baseY) * TOOLTIP_INDICATOR_TIP_START

        lineTo(entryX, baseY)
        lineTo(tipEntryX, tipY)
        cubicTo(
            x1 = tipEntryX + (centerX - tipEntryX) * TOOLTIP_INDICATOR_TIP_CURVATURE,
            y1 = tipY + (apexY - tipY) * TOOLTIP_INDICATOR_TIP_CURVATURE,
            x2 = tipExitX + (centerX - tipExitX) * TOOLTIP_INDICATOR_TIP_CURVATURE,
            y2 = tipY + (apexY - tipY) * TOOLTIP_INDICATOR_TIP_CURVATURE,
            x3 = tipExitX,
            y3 = tipY,
        )
        lineTo(exitX, baseY)
    }

    private companion object {
        const val QUARTER_TURN = 90f
        const val HALF_TURN = 180f
    }
}

// MARK: - Previews

private data class TooltipPreviewData(
    val indicatorPlacement: TooltipIndicatorPlacement,
    val withTitle: Boolean,
    val withCloseButton: Boolean,
    val withCover: Boolean,
    val withFooter: Boolean,
)

private class TooltipPreviewProvider : PreviewParameterProvider<TooltipPreviewData> {
    override val values: Sequence<TooltipPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<TooltipPreviewData> =
        buildList {
            TooltipIndicatorPlacement.entries.forEach { placement ->
                add(
                    element = TooltipPreviewData(
                        indicatorPlacement = placement,
                        withTitle = true,
                        withCloseButton = false,
                        withCover = false,
                        withFooter = true,
                    ),
                )
            }
            listOf(true, false).forEach { withTitle ->
                listOf(true, false).forEach { withCover ->
                    add(
                        element = TooltipPreviewData(
                            indicatorPlacement = TooltipIndicatorPlacement.TopCenter,
                            withTitle = withTitle,
                            withCloseButton = true,
                            withCover = withCover,
                            withFooter = withTitle,
                        ),
                    )
                }
            }
        }.asSequence()
}

@LemonadePreview
@Composable
private fun TooltipPreview(
    @PreviewParameter(TooltipPreviewProvider::class)
    previewData: TooltipPreviewData,
) {
    val onCloseClick: (() -> Unit)? = if (previewData.withCloseButton) {
        {}
    } else {
        null
    }

    LemonadeUi.Tooltip(
        content = "Tap here to see everything you sold today.",
        title = "Daily takings".takeIf { previewData.withTitle },
        indicatorPlacement = previewData.indicatorPlacement,
        onCloseClick = onCloseClick,
        closeContentDescription = "Close",
        cover = if (previewData.withCover) {
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = LocalColors.current.background.bgBrandSubtle),
                )
            }
        } else {
            null
        },
        footer = if (previewData.withFooter) {
            {
                StepCounter(
                    currentStep = 1,
                    totalSteps = 3,
                    modifier = Modifier.weight(weight = 1f),
                )
                Action(
                    label = "Skip",
                    onClick = {},
                    variant = TooltipFooterActionVariant.Secondary,
                )
                Action(
                    label = "Next",
                    onClick = {},
                )
            }
        } else {
            null
        },
    )
}
