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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
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
            color = colors.content.contentPrimaryInverse.copy(alpha = opacities.base.opacity80),
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
            TooltipFooterActionVariant.Primary -> colors.background.bgDefault
            TooltipFooterActionVariant.Secondary -> Color.Transparent
        }
        val contentColor = when (variant) {
            TooltipFooterActionVariant.Primary -> colors.content.contentPrimary
            TooltipFooterActionVariant.Secondary -> colors.content.contentPrimaryInverse
        }
        val textStyle = when (variant) {
            TooltipFooterActionVariant.Primary -> typographies.bodySmallSemiBold
            TooltipFooterActionVariant.Secondary -> typographies.bodySmallMedium
        }
        val borderModifier = when (variant) {
            TooltipFooterActionVariant.Primary -> Modifier
            TooltipFooterActionVariant.Secondary -> Modifier.border(
                width = LocalBorderWidths.current.base.border25,
                color = colors.border.borderNeutralMediumInverse,
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

    // Opaque rather than the design's translucent bg-always-dark: without a backdrop blur a
    // ~74% fill lets busy content read straight through the text, and blurring it on Compose needs
    // a third-party dependency that is not worth carrying for one component.
    val surfaceColor = colors.background.bgDefaultInverse
    Box(
        modifier = modifier
            .width(width = TooltipWidth)
            // Matches the `isolate` on Figma's tooltip root: without its own compositing layer the
            // close button's blend would reach through to whatever is behind the tooltip.
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
    ) {
        // The shadow is cast from the body rectangle rather than the full tooltip outline. Given an
        // Outline.Generic shape, Compose's dropShadow clips the blur a few dp past the node bounds
        // on Android, which cuts the falloff off square; a rounded rect takes the un-clipped path.
        // The indicator goes unshadowed, which at this token's 5% alpha over 8dp is not visible.
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    top = indicatorTopInset,
                    bottom = indicatorBottomInset,
                ).lemonadeShadow(
                    shadow = LemonadeShadow.Xlarge,
                    shape = LocalShapes.current.radius600,
                ),
        )

        Column(
            modifier = Modifier
                .background(
                    color = surfaceColor,
                    shape = shape,
                ).padding(
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            // Only takes effect when there is a title — spacedBy adds nothing to a single child.
            verticalArrangement = Arrangement.spacedBy(space = spaces.spacing100),
        ) {
            if (title != null) {
                LemonadeUi.Text(
                    text = title,
                    textStyle = typographies.bodySmallSemiBold,
                    color = colors.content.contentPrimaryInverse,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            LemonadeUi.Text(
                text = content,
                textStyle = typographies.bodySmallRegular,
                color = colors.content.contentPrimaryInverse.copy(alpha = opacities.base.opacity90),
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
            ).graphicsLayer {
                alpha = opacities.base.opacity40
                // Hard light rather than a flat tint: over the dark light-mode surface a light icon
                // screens and stays visible, and over the light dark-mode surface a dark icon
                // multiplies, so the same token reads on both without a per-theme colour.
                blendMode = BlendMode.Hardlight
            },
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
 * The tooltip surface: a rounded rectangle with the indicator triangle added to one edge.
 *
 * The two are separate sub-paths of a single [Path] rather than two drawn shapes. Non-zero winding
 * fills overlapping sub-paths as their union in one operation, so the join is seamless — drawing
 * them as two views at the surface's ~74% alpha would composite the overlap twice and show a darker
 * wedge across the join. Being one shape also means one shadow outline and one clip for the host's
 * backdrop blur.
 *
 * The indicator is drawn outside the body, so the shape expects a size that already includes
 * [TooltipIndicatorHeight] on the indicator's edge.
 */
internal data class TooltipShape(
    private val indicatorPlacement: TooltipIndicatorPlacement,
    private val cornerRadius: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val indicatorHeight = with(density) { TooltipIndicatorHeight.toPx() }
        val bodyTop = if (indicatorPlacement.pointsUp) indicatorHeight else 0f
        val bodyBottom = if (indicatorPlacement.pointsDown) {
            size.height - indicatorHeight
        } else {
            size.height
        }
        val radius = with(density) { cornerRadius.toPx() }
            .coerceAtMost(maximumValue = minOf(size.width, bodyBottom - bodyTop) / 2f)

        val body = Path().apply {
            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(
                        left = 0f,
                        top = bodyTop,
                        right = size.width,
                        bottom = bodyBottom,
                    ),
                    cornerRadius = CornerRadius(radius, radius),
                ),
            )
        }

        if (indicatorPlacement == TooltipIndicatorPlacement.None) {
            return Outline.Generic(path = body)
        }

        val indicator = indicatorPath(
            size = size,
            density = density,
            baseY = if (indicatorPlacement.pointsUp) bodyTop else bodyBottom,
        )

        // An explicit union rather than appending the triangle as a second sub-path. Skia adds the
        // round rect with the opposite winding to the triangle, so under the non-zero rule the two
        // cancel exactly where they overlap and punch a 1px hole the width of the indicator base
        // across the join. A boolean union does not care about winding.
        val path = Path().apply {
            op(
                path1 = body,
                path2 = indicator,
                operation = PathOperation.Union,
            )
        }

        return Outline.Generic(path = path)
    }

    /**
     * The indicator triangle. Its straight edges stop short of the notional apex and a cubic bridges
     * the gap, which is what rounds the tip. The base is deliberately sunk one pixel into the body
     * so the two sub-paths overlap rather than merely touching, which would leave a hairline.
     */
    private fun indicatorPath(
        size: Size,
        density: Density,
        baseY: Float,
    ): Path {
        val halfBase = with(density) { TooltipIndicatorBaseWidth.toPx() } / 2f
        val apexHeight = with(density) { TooltipIndicatorApexHeight.toPx() }
        val edgeInset = with(density) { TooltipIndicatorEdgeInset.toPx() }

        val pointsUp = indicatorPlacement.pointsUp
        val centerX = when (indicatorPlacement) {
            TooltipIndicatorPlacement.TopLeft,
            TooltipIndicatorPlacement.BottomLeft,
            -> edgeInset + halfBase

            TooltipIndicatorPlacement.TopRight,
            TooltipIndicatorPlacement.BottomRight,
            -> size.width - edgeInset - halfBase

            else -> size.width / 2f
        }

        val overlap = if (pointsUp) 1f else -1f
        val apexY = if (pointsUp) baseY - apexHeight else baseY + apexHeight
        val tipX = centerX
        val tipEntryX = (centerX - halfBase) + halfBase * TOOLTIP_INDICATOR_TIP_START
        val tipExitX = (centerX + halfBase) - halfBase * TOOLTIP_INDICATOR_TIP_START
        val tipY = baseY + (apexY - baseY) * TOOLTIP_INDICATOR_TIP_START
        val controlY = tipY + (apexY - tipY) * TOOLTIP_INDICATOR_TIP_CURVATURE

        return Path().apply {
            moveTo(centerX - halfBase, baseY + overlap)
            lineTo(tipEntryX, tipY)
            cubicTo(
                x1 = tipEntryX + (tipX - tipEntryX) * TOOLTIP_INDICATOR_TIP_CURVATURE,
                y1 = controlY,
                x2 = tipExitX + (tipX - tipExitX) * TOOLTIP_INDICATOR_TIP_CURVATURE,
                y2 = controlY,
                x3 = tipExitX,
                y3 = tipY,
            )
            lineTo(centerX + halfBase, baseY + overlap)
            close()
        }
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
