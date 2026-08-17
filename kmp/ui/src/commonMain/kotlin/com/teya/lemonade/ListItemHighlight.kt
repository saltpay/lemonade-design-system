package com.teya.lemonade

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.LayoutDirection
import com.teya.lemonade.core.LemonadeListItemVoice
import kotlinx.coroutines.launch

/**
 * Matches the default spring of `animateColorAsState`, which the highlight previously used. The
 * node animates a 0..1 fraction and scales the theme colour's alpha at draw time; for a same-RGB
 * alpha fade this is trajectory-identical to animating the [Color] itself, since springs act on
 * each vector dimension independently.
 */
private val HighlightAnimationSpec: SpringSpec<Float> = spring()

/**
 * Press/hover indication for interactive list items.
 *
 * The highlight is painted directly in the draw phase, reading the theme colour and shape at draw
 * time (not composition time) and using the row's rounded [Shape] outline instead of a
 * [androidx.compose.ui.draw.clip] graphics layer. While a row is idle or scrolling the fraction is
 * zero, so the row draws nothing and pays no clip/background cost. There is no platform ripple;
 * press feedback is the animated fill, mirroring the iOS `ListItemButtonStyle`.
 *
 * Interaction tracking lives inside the indication [Modifier.Node] rather than in composition, so
 * pressing or hovering a row never recomposes it — the animation only invalidates draw. Rows that
 * are never touched allocate no animation state at all.
 */
internal data class ListItemHighlightIndication(
    private val voice: LemonadeListItemVoice,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        ListItemHighlightNode(
            interactionSource = interactionSource,
            voice = voice,
        )
}

private class ListItemHighlightNode(
    private val interactionSource: InteractionSource,
    private val voice: LemonadeListItemVoice,
) : Modifier.Node(),
    DrawModifierNode,
    CompositionLocalConsumerModifierNode {
    private val pressInteractions = mutableListOf<PressInteraction.Press>()
    private val hoverInteractions = mutableListOf<HoverInteraction.Enter>()

    private var highlightFraction: Animatable<Float, AnimationVector1D>? = null

    private var cachedOutline: Outline? = null
    private var cachedSize: Size = Size.Unspecified
    private var cachedLayoutDirection: LayoutDirection? = null
    private var cachedShape: Shape? = null

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> pressInteractions.add(interaction)
                    is PressInteraction.Release -> pressInteractions.remove(interaction.press)
                    is PressInteraction.Cancel -> pressInteractions.remove(interaction.press)
                    is HoverInteraction.Enter -> hoverInteractions.add(interaction)
                    is HoverInteraction.Exit -> hoverInteractions.remove(interaction.enter)
                    else -> return@collect
                }
                animateHighlight()
            }
        }
    }

    override fun onDetach() {
        pressInteractions.clear()
        hoverInteractions.clear()
        highlightFraction = null
        cachedOutline = null
        cachedSize = Size.Unspecified
        cachedLayoutDirection = null
        cachedShape = null
    }

    private fun animateHighlight() {
        val active = pressInteractions.isNotEmpty() || hoverInteractions.isNotEmpty()
        val target = if (active) 1f else 0f
        val animatable = highlightFraction
            ?: if (active) {
                Animatable(initialValue = 0f).also { created ->
                    highlightFraction = created
                }
            } else {
                return
            }
        if (animatable.targetValue == target) {
            return
        }
        coroutineScope.launch {
            animatable.animateTo(
                targetValue = target,
                animationSpec = HighlightAnimationSpec,
            ) {
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawHighlight()
        drawContent()
    }

    private fun ContentDrawScope.drawHighlight() {
        val fraction = highlightFraction?.value
            ?: return
        if (fraction <= 0f) {
            return
        }
        val baseColor = highlightColor()
        val alpha = baseColor.alpha * fraction
        if (alpha <= 0f) {
            return
        }
        drawOutline(
            outline = obtainOutline(),
            color = baseColor.copy(alpha = alpha),
        )
    }

    private fun highlightColor(): Color {
        val interactionColors = currentValueOf(LocalColors).interaction
        return when (voice) {
            LemonadeListItemVoice.Neutral -> interactionColors.bgSubtleInteractive
            LemonadeListItemVoice.Critical -> interactionColors.bgCriticalSubtleInteractive
        }
    }

    private fun ContentDrawScope.obtainOutline(): Outline {
        val shape = currentValueOf(LocalShapes).radius500
        val existing = cachedOutline
        val cacheValid = cachedSize == size &&
            cachedLayoutDirection == layoutDirection &&
            cachedShape == shape
        if (existing != null && cacheValid) {
            return existing
        }
        val outline = shape.createOutline(
            size = size,
            layoutDirection = layoutDirection,
            density = this,
        )
        cachedOutline = outline
        cachedSize = size
        cachedLayoutDirection = layoutDirection
        cachedShape = shape
        return outline
    }
}
