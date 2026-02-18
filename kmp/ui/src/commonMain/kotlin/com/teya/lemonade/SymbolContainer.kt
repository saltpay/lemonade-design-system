package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A versatile container used to display an icon, brand logo, or image.
 *  Supports consistent sizing and different tone of voice.
 * ## Usage
 * ```kotlin
 * LemonadeUi.SymbolContainer(
 *     icon = LemonadeIcons.Heart,
 *     contentDescription = "Content Description",
 *     voice = SymbolContainerVoice.Info,
 *     size = SymbolContainerSize.Small,
 * )
 * ```
 * @param icon - [LemonadeIcons] to be displayed inside the container.
 * @param contentDescription - the **localized** content description for the [icon].
 * @param modifier - Optional, the [Modifier] to be applied to the base component.
 * @param voice - [SymbolContainerVoice] to define the tone of voice. This will effectively define
 *  color of the background and the tint for the [icon]. Defaults to [SymbolContainerVoice.Neutral].
 * @param size - [SymbolContainerSize] to define the container's size. Defaults to [SymbolContainerSize.Medium].
 */
@Composable
public fun LemonadeUi.SymbolContainer(
    icon: LemonadeIcons,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
) {
    CoreSymbolContainer(
        voice = voice,
        size = size,
        modifier = modifier,
        contentSlot = {
            LemonadeUi.Icon(
                icon = icon,
                size = LocalSymbolContainerPlatformDimensions.current.lemonadeIconSize,
                contentDescription = contentDescription,
                tint = voice.tintColor,
            )
        },
    )
}

/**
 * A versatile container used to display an icon, brand logo, or image.
 *  Supports consistent sizing and different tone of voice.
 * ## Usage
 * ```kotlin
 * LemonadeUi.SymbolContainer(
 *     text = "W",
 *     voice = SymbolContainerVoice.Info,
 *     size = SymbolContainerSize.Small,
 * )
 * ```
 * @param text - [Text] to be displayed inside the container.
 * @param modifier - Optional, the [Modifier] to be applied to the base component.
 * @param voice - [SymbolContainerVoice] to define the tone of voice. This will effectively define
 *  color of the background and the color for the [text]. Defaults to [SymbolContainerVoice.Neutral].
 * @param size - [SymbolContainerSize] to define the container's size. Defaults to [SymbolContainerSize.Medium].
 */
@Composable
public fun LemonadeUi.SymbolContainer(
    text: String,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
) {
    CoreSymbolContainer(
        voice = voice,
        size = size,
        modifier = modifier,
        contentSlot = {
            LemonadeUi.Text(
                text = text,
                color = voice.tintColor,
                textStyle = LocalSymbolContainerPlatformDimensions.current.textStyle,
            )
        },
    )
}

/**
 * A versatile container used to display an icon, brand logo, or image.
 *  Supports consistent sizing and different tone of voice.
 * ## Usage
 * ```kotlin
 * LemonadeUi.SymbolContainer(
 *     voice = SymbolContainerVoice.Info,
 *     size = SymbolContainerSize.Small,
 *     contentSlot = {
 *          Image(
 *              painter = ...,
 *              ...
 *          )
 *     },
 * )
 * ```
 * @param contentSlot - A Composable slot for generic content to be added as the content of the container.
 * @param modifier - Optional, the [Modifier] to be applied to the base component.
 * @param voice - [SymbolContainerVoice] to define the tone of voice. This will effectively define
 *  color of the background. Defaults to [SymbolContainerVoice.Neutral].
 * @param size - [SymbolContainerSize] to define the container's size. Defaults to [SymbolContainerSize.Medium].
 */
@Composable
public fun LemonadeUi.SymbolContainer(
    contentSlot: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
) {
    CoreSymbolContainer(
        voice = voice,
        size = size,
        modifier = modifier,
        contentSlot = {
            Box(
                content = contentSlot,
                contentAlignment = Alignment.Center,
                modifier = Modifier.requiredSize(
                    size = LocalSymbolContainerPlatformDimensions.current.contentSize,
                ),
            )
        },
    )
}

@Composable
private fun CoreSymbolContainer(
    contentSlot: @Composable BoxScope.() -> Unit,
    voice: SymbolContainerVoice,
    size: SymbolContainerSize,
    modifier: Modifier = Modifier,
) {
    val dimensions = size.defaultSymbolContainerPlatformDimensions()
    CompositionLocalProvider(LocalSymbolContainerPlatformDimensions provides dimensions) {
        Box(
            content = contentSlot,
            contentAlignment = Alignment.Center,
            modifier = modifier
                .clip(shape = LocalShapes.current.radiusFull)
                .background(color = voice.containerColor)
                .requiredSize(size = dimensions.containerSize),
        )
    }
}

private val LocalSymbolContainerPlatformDimensions =
    staticCompositionLocalOf<SymbolContainerPlatformDimensions> {
        error("Local Symbol container platform dimensions not initialized")
    }

private val SymbolContainerVoice.tintColor: Color
    @Composable get() {
        return when (this) {
            SymbolContainerVoice.Neutral -> LocalColors.current.content.contentPrimary
            SymbolContainerVoice.Critical -> LocalColors.current.content.contentCritical
            SymbolContainerVoice.Warning -> LocalColors.current.content.contentCaution
            SymbolContainerVoice.Info -> LocalColors.current.content.contentInfo
            SymbolContainerVoice.Positive -> LocalColors.current.content.contentPositive
            SymbolContainerVoice.Brand -> LocalColors.current.content.contentOnBrandHigh
            SymbolContainerVoice.BrandSubtle -> LocalColors.current.content.contentOnBrandHigh
        }
    }

private val SymbolContainerVoice.containerColor: Color
    @Composable get() {
        return when (this) {
            SymbolContainerVoice.Neutral -> LocalColors.current.background.bgNeutralSubtle
            SymbolContainerVoice.Critical -> LocalColors.current.background.bgCriticalSubtle
            SymbolContainerVoice.Warning -> LocalColors.current.background.bgCautionSubtle
            SymbolContainerVoice.Info -> LocalColors.current.background.bgInfoSubtle
            SymbolContainerVoice.Positive -> LocalColors.current.background.bgPositiveSubtle
            SymbolContainerVoice.Brand -> LocalColors.current.background.bgBrand
            SymbolContainerVoice.BrandSubtle -> LocalColors.current.background.bgBrandSubtle
        }
    }

private data class SymbolContainerPlatformDimensions(
    val containerSize: Dp,
    val contentSize: Dp,
    val lemonadeIconSize: LemonadeAssetSize,
    val textStyle: LemonadeTextStyle,
)

@Composable
private fun SymbolContainerSize.defaultSymbolContainerPlatformDimensions(): SymbolContainerPlatformDimensions =
    when (this) {
        SymbolContainerSize.XSmall -> SymbolContainerPlatformDimensions(
            containerSize = LocalSizes.current.size600,
            contentSize = LocalSizes.current.size300,
            lemonadeIconSize = LemonadeAssetSize.XSmall,
            textStyle = LocalTypographies.current.bodyXSmallSemiBold,
        )

        SymbolContainerSize.Small -> SymbolContainerPlatformDimensions(
            containerSize = LocalSizes.current.size800,
            contentSize = LocalSizes.current.size400,
            lemonadeIconSize = LemonadeAssetSize.Small,
            textStyle = LocalTypographies.current.bodySmallSemiBold,
        )

        SymbolContainerSize.Medium -> SymbolContainerPlatformDimensions(
            containerSize = LocalSizes.current.size1000,
            contentSize = LocalSizes.current.size500,
            lemonadeIconSize = LemonadeAssetSize.Medium,
            textStyle = LocalTypographies.current.bodySmallSemiBold,
        )

        SymbolContainerSize.Large -> SymbolContainerPlatformDimensions(
            containerSize = LocalSizes.current.size1200,
            contentSize = LocalSizes.current.size600,
            lemonadeIconSize = LemonadeAssetSize.Large,
            textStyle = LocalTypographies.current.bodyLargeSemiBold,
        )

        SymbolContainerSize.XLarge -> SymbolContainerPlatformDimensions(
            containerSize = LocalSizes.current.size1600,
            contentSize = LocalSizes.current.size800,
            lemonadeIconSize = LemonadeAssetSize.XLarge,
            textStyle = LocalTypographies.current.bodyXLargeSemiBold,
        )
    }

private data class SymbolContainerPreviewData(
    val content: Any,
    val size: SymbolContainerSize,
    val voice: SymbolContainerVoice,
)

private class SymbolContainerPreviewProvider :
    PreviewParameterProvider<SymbolContainerPreviewData> {
    override val values: Sequence<SymbolContainerPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<SymbolContainerPreviewData> =
        buildList {
            SymbolContainerVoice.entries.forEach { voice ->
                listOf("A", LemonadeIcons.Heart).forEach { content ->
                    SymbolContainerSize.entries.forEach { size ->
                        add(
                            SymbolContainerPreviewData(
                                content = content,
                                size = size,
                                voice = voice,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun SymbolContainerPreview(
    @PreviewParameter(SymbolContainerPreviewProvider::class)
    previewData: SymbolContainerPreviewData,
) {
    when (previewData.content) {
        is LemonadeIcons -> {
            LemonadeUi.SymbolContainer(
                icon = previewData.content,
                size = previewData.size,
                voice = previewData.voice,
                contentDescription = "Content Description",
            )
        }

        is String -> {
            LemonadeUi.SymbolContainer(
                text = previewData.content,
                size = previewData.size,
                voice = previewData.voice,
            )
        }
    }
}
