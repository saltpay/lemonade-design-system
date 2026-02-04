package com.teya.lemonade

import com.teya.lemonade.core.LemonadeAsset
import com.teya.lemonade.core.LemonadeBrandLogos
import com.teya.lemonade.core.LemonadeCountryFlags
import com.teya.lemonade.core.LemonadeIcons
import org.jetbrains.compose.resources.DrawableResource

public val LemonadeAsset.drawableResource: DrawableResource
    get() {
        return when (this) {
            is LemonadeIcons -> this.drawableResource
            is LemonadeBrandLogos -> this.drawableResource
            is LemonadeCountryFlags -> this.drawableResource
        }
    }
