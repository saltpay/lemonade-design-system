package com.teya.lemonade

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCountryFlags
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Country Flags component, to display the available country flags in standardized way.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.CountryFlag(
 *     logo = LemonadeCountryFlags.BRBrazil,
 *     size = LemonadeBrandLogoSize.Medium,
 *     modifier = Modifier.clickable{ ... },
 * )
 * ```
 *
 * @param flag - The [LemonadeCountryFlags] to be displayed.
 * @param contentDescription - The localizable message to be shown as content description for the [flag]. Defaults to [LemonadeCountryFlags.name].
 * @param size - The [LemonadeAssetSize] to be applied to the flag. Defaults to [LemonadeAssetSize.Medium]
 * @param Modifier - Optional [Modifier] for additional styling and layout adjustments.
 */
@Composable
public fun LemonadeUi.CountryFlag(
    flag: LemonadeCountryFlags,
    contentDescription: String = flag.name,
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    modifier: Modifier = Modifier,
) {
    CoreCountryFlag(
        flag = flag,
        contentDescription = contentDescription,
        size = size,
        modifier = modifier,
    )
}

@Composable
public fun CoreCountryFlag(
    flag: LemonadeCountryFlags,
    size: LemonadeAssetSize,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(resource = flag.drawableResource),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(shape = LocalShapes.current.radiusFull)
            .border(
                width = LocalBorderWidths.current.base.border25,
                color = LocalColors.current.border.borderNeutralMedium,
                shape = LocalShapes.current.radiusFull,
            )
            .requiredSize(size = size.dp),
    )
}

private val LemonadeAssetSize.dp: Dp
    @Composable get() {
        return when (this) {
            LemonadeAssetSize.XSmall -> LocalSizes.current.size400
            LemonadeAssetSize.Small -> LocalSizes.current.size400
            LemonadeAssetSize.Medium -> LocalSizes.current.size500
            LemonadeAssetSize.Large -> LocalSizes.current.size600
            LemonadeAssetSize.XLarge -> LocalSizes.current.size800
            LemonadeAssetSize.XXLarge -> LocalSizes.current.size1000
            LemonadeAssetSize.XXXLarge -> LocalSizes.current.size1200
        }
    }

private data class CountryFlagPreviewData(
    val flag: LemonadeCountryFlags,
    val size: LemonadeAssetSize,
)

private class CountryFlagPreviewProvider : PreviewParameterProvider<CountryFlagPreviewData> {
    override val values: Sequence<CountryFlagPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<CountryFlagPreviewData> {
        return buildList {
            LemonadeCountryFlags.entries.take(5).forEach { flag ->
                LemonadeAssetSize.entries.forEach { size ->
                    add(
                        CountryFlagPreviewData(
                            flag = flag,
                            size = size,
                        )
                    )
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun CountryFlagPreview(
    @PreviewParameter(CountryFlagPreviewProvider::class)
    previewData: CountryFlagPreviewData,
) {
    LemonadeUi.CountryFlag(
        flag = previewData.flag,
        size = previewData.size,
    )
}
