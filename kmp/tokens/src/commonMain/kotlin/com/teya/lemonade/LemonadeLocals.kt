package com.teya.lemonade

import androidx.compose.foundation.Indication
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.LemonadeTypography

public val LocalColors: ProvidableCompositionLocal<LemonadeSemanticColors> =
    staticCompositionLocalOf {
        error("No default colors set. Wrap your content in LemonadeTheme.")
    }

public val LocalTypographies: ProvidableCompositionLocal<LemonadeTypographyProvider> =
    staticCompositionLocalOf {
        LemonadeTypographyProvider()
    }

public val LocalContentColors: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    error("No default Content Colors set in the LocalContentColors for theme")
}

public val LocalTextStyles: ProvidableCompositionLocal<LemonadeTextStyle> =
    staticCompositionLocalOf {
        LemonadeTypography.BodyMediumRegular.style
    }

public val LocalRadius: ProvidableCompositionLocal<LemonadeRadiusValues> =
    staticCompositionLocalOf {
        InternalLemonadeRadiusValues()
    }

public val LocalShapes: ProvidableCompositionLocal<LemonadeShapes> = staticCompositionLocalOf {
    InternalLemonadeShapes()
}

public val LocalSpaces: ProvidableCompositionLocal<LemonadeSpaceValues> =
    staticCompositionLocalOf {
        InternalLemonadeSpaceValues()
    }

public val LocalOpacities: ProvidableCompositionLocal<LemonadeOpacity> =
    staticCompositionLocalOf {
        InternalLemonadeOpacityTokens()
    }

public val LocalBorderWidths: ProvidableCompositionLocal<LemonadeBorderWidth> =
    staticCompositionLocalOf {
        InternalLemonadeBorderWidth()
    }

public val LocalSizes: ProvidableCompositionLocal<LemonadeSizeValues> = staticCompositionLocalOf {
    InternalLemonadeSizeValues()
}

public val LocalEffects: ProvidableCompositionLocal<LemonadeEffects> = staticCompositionLocalOf {
    object : LemonadeEffects {
        override val interactionIndication: Indication? = null
    }
}
