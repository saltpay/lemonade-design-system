package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teya.lemonade.core.LemonadeAsset
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeBrandLogos
import com.teya.lemonade.core.LemonadeCountryFlags
import com.teya.lemonade.core.LemonadeIcons

/**
 * Generic Asset component, to display the available assets in standardized way.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Asset(
 *     asset = LemonadeCountryFlags.BRBrazil,
 *     size = LemonadeAssetSize.Medium,
 *     modifier = Modifier.clickable{ ... },
 * )
 * ```
 *
 * @param asset - The [LemonadeAsset] to be displayed. e.g.: [LemonadeIcons], [LemonadeCountryFlags], [LemonadeBrandLogos], ...
 * @param contentDescription - The localizable message to description for the asset.
 * @param size - The [LemonadeAssetSize] to be applied to the flag. Defaults to [LemonadeAssetSize.Medium]
 * @param Modifier - Optional [Modifier] for additional styling and layout adjustments.
 * @param tint - Optional [Color] for tinting the asset. The tint is only applied if the corresponding asset allows it.
 */
@Composable
public fun LemonadeUi.Asset(
    asset: LemonadeAsset,
    size: LemonadeAssetSize,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    when (asset) {
        is LemonadeIcons -> {
            LemonadeUi.Icon(
                icon = asset,
                contentDescription = contentDescription,
                tint = tint,
                size = size,
                modifier = modifier,
            )
        }

        is LemonadeBrandLogos -> {
            LemonadeUi.BrandLogo(
                logo = asset,
                contentDescription = contentDescription ?: asset.name,
                size = size,
                modifier = modifier,
            )
        }

        is LemonadeCountryFlags -> {
            LemonadeUi.CountryFlag(
                flag = asset,
                contentDescription = contentDescription ?: asset.name,
                size = size,
                modifier = modifier,
            )
        }
    }
}