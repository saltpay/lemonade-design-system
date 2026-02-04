package com.teya.lemonade

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeBrandLogos
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Brand logo component, to indication of Card Schemes in standardized way.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.BrandLogo(
 *     logo = LemonadeBrandLogo.Visa,
 *     size = LemonadeBrandLogoSize.Medium,
 *     modifier = Modifier.clickable{ ... },
 * )
 * ```
 *
 * @param logo - The [LemonadeBrandLogos] to be displayed.
 * @param contentDescription - The localizable content description for the [logo]. Defaults to [LemonadeBrandLogos.name].
 * @param size - The [LemonadeAssetSize] to be applied to the logo. Defaults to [LemonadeAssetSize.Medium]
 * @param Modifier - Optional [Modifier] for additional styling and layout adjustments.
 */
@Composable
public fun LemonadeUi.BrandLogo(
    logo: LemonadeBrandLogos,
    contentDescription: String = logo.name,
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    modifier: Modifier = Modifier,
) {
    CoreBrandLogo(
        logo = logo,
        size = size,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@Composable
private fun CoreBrandLogo(
    logo: LemonadeBrandLogos,
    size: LemonadeAssetSize,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(resource = logo.drawableResource),
        contentDescription = contentDescription,
        modifier = modifier.requiredSize(size = size.dp),
    )
}

private val LemonadeAssetSize.dp: Dp
    @Composable get() {
        return when (this) {
            LemonadeAssetSize.XSmall -> LocalSizes.current.size300
            LemonadeAssetSize.Small -> LocalSizes.current.size400
            LemonadeAssetSize.Medium -> LocalSizes.current.size500
            LemonadeAssetSize.Large -> LocalSizes.current.size600
            LemonadeAssetSize.XLarge -> LocalSizes.current.size800
            LemonadeAssetSize.XXLarge -> LocalSizes.current.size1000
            LemonadeAssetSize.XXXLarge -> LocalSizes.current.size1200
        }
    }

private data class BrandLogoPreviewData(
    val logo: LemonadeBrandLogos,
    val size: LemonadeAssetSize,
)

private class BrandLogoPreviewProvider : PreviewParameterProvider<BrandLogoPreviewData> {
    override val values: Sequence<BrandLogoPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<BrandLogoPreviewData> {
        return buildList {
            LemonadeBrandLogos.entries.take(5).forEach { logo ->
                LemonadeAssetSize.entries.forEach { size ->
                    add(
                        BrandLogoPreviewData(
                            logo = logo,
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
    @PreviewParameter(BrandLogoPreviewProvider::class)
    previewData: BrandLogoPreviewData,
) {
    LemonadeUi.BrandLogo(
        logo = previewData.logo,
        size = previewData.size,
    )
}
