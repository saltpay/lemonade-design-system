package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.ToastVoice

/**
 * A brief notification that appears temporarily to provide feedback or information.
 * Similar to Snackbar but typically voice-based with Info, Warning, Critical, or Positive tones.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Toast(
 *     message = "Payment successful",
 *     voice = ToastVoice.Positive,
 * )
 * ```
 *
 * @param message - [String] to be displayed in the toast.
 * @param modifier - [Modifier] to be applied to the root container of the toast.
 * @param voice - [ToastVoice] to define the tone of voice. This will effectively define
 *  color of the background alongside the text's and icon's tints. Defaults to [ToastVoice.Neutral].
 * @param leadingIcon - optional [LemonadeIcons] to show as leading icon in the toast.
 *  Defaults to the voice's default icon.
 */
@Composable
public fun LemonadeUi.Toast(
    message: String,
    modifier: Modifier = Modifier,
    voice: ToastVoice = ToastVoice.Neutral,
    leadingIcon: LemonadeIcons? = voice.defaultIcon,
) {
    CoreToast(
        message = message,
        voice = voice,
        leadingIcon = leadingIcon,
        modifier = modifier,
    )
}

@Composable
private fun CoreToast(
    message: String,
    voice: ToastVoice,
    leadingIcon: LemonadeIcons?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
        modifier = modifier
            .background(
                color = voice.containerColor,
                shape = LocalShapes.current.radius300,
            )
            .padding(
                vertical = LocalSpaces.current.spacing200,
                horizontal = LocalSpaces.current.spacing300,
            ),
    ) {
        if (leadingIcon != null) {
            LemonadeUi.Icon(
                icon = leadingIcon,
                contentDescription = null,
                tint = voice.tintColor,
                size = LemonadeAssetSize.Small,
            )
        }

        LemonadeUi.Text(
            text = message,
            color = voice.tintColor,
            overflow = TextOverflow.Ellipsis,
            textStyle = LocalTypographies.current.bodySmallMedium,
            maxLines = 1,
        )
    }
}

private val ToastVoice.tintColor: Color
    @Composable get() {
        return when (this) {
            ToastVoice.Neutral -> LocalColors.current.content.contentPrimary
            ToastVoice.Info -> LocalColors.current.content.contentInfo
            ToastVoice.Warning -> LocalColors.current.content.contentCaution
            ToastVoice.Critical -> LocalColors.current.content.contentCritical
            ToastVoice.Positive -> LocalColors.current.content.contentPositive
        }
    }

private val ToastVoice.containerColor: Color
    @Composable get() {
        return when (this) {
            ToastVoice.Neutral -> LocalColors.current.background.bgNeutralSubtle
            ToastVoice.Info -> LocalColors.current.background.bgInfoSubtle
            ToastVoice.Warning -> LocalColors.current.background.bgCautionSubtle
            ToastVoice.Critical -> LocalColors.current.background.bgCriticalSubtle
            ToastVoice.Positive -> LocalColors.current.background.bgPositiveSubtle
        }
    }

private val ToastVoice.defaultIcon: LemonadeIcons?
    @Composable get() {
        return when (this) {
            ToastVoice.Neutral -> null
            ToastVoice.Info -> LemonadeIcons.CircleInfo
            ToastVoice.Warning -> LemonadeIcons.TriangleAlert
            ToastVoice.Critical -> LemonadeIcons.CircleAlert
            ToastVoice.Positive -> LemonadeIcons.CircleCheck
        }
    }
