package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TagVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A compact label used to categorise, organise, or annotate content.
 *  Typically static and non-interactive.
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tag(
 *     icon = LemonadeIcons.Warning,
 *     label = "WARNING",
 *     voice = TagVoice.Caution,
 * )
 * ```
 * @param label - [String] to be displayed in the tag.
 * @param modifier - [Modifier] to be applied to the root container of the tag.
 * @param icon - optional [LemonadeIcons] to show as leading icon in the tag.
 * @param voice - [TagVoice] to define the tone of voice. This will effectively define
 *  color of the background alongside the text's and icon's tints. Defaults to [TagVoice.Neutral].
 */
@Composable
public fun LemonadeUi.Tag(
    label: String,
    modifier: Modifier = Modifier,
    icon: LemonadeIcons? = null,
    voice: TagVoice = TagVoice.Neutral,
) {
    CoreTag(
        label = label,
        icon = icon,
        voice = voice,
        modifier = modifier,
    )
}

@Composable
private fun CoreTag(
    label: String,
    icon: LemonadeIcons?,
    voice: TagVoice,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing50),
        modifier = modifier
            .background(
                color = voice.containerColor,
                shape = LocalShapes.current.radius100,
            ).padding(
                vertical = LocalSpaces.current.spacing50,
                horizontal = LocalSpaces.current.spacing100,
            ),
    ) {
        if (icon != null) {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = null,
                tint = voice.tintColor,
                size = LemonadeAssetSize.Small,
            )
        }

        LemonadeUi.Text(
            text = label,
            color = voice.tintColor,
            overflow = TextOverflow.Ellipsis,
            textStyle = LocalTypographies.current.bodyXSmallSemiBold,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing50),
        )
    }
}

private val TagVoice.tintColor: Color
    @Composable get() {
        return when (this) {
            TagVoice.Neutral -> LocalColors.current.content.contentPrimary
            TagVoice.Critical -> LocalColors.current.content.contentCritical
            TagVoice.Warning -> LocalColors.current.content.contentCaution
            TagVoice.Info -> LocalColors.current.content.contentInfo
            TagVoice.Positive -> LocalColors.current.content.contentPositive
        }
    }

private val TagVoice.containerColor: Color
    @Composable get() {
        return when (this) {
            TagVoice.Neutral -> LocalColors.current.background.bgNeutralSubtle
            TagVoice.Critical -> LocalColors.current.background.bgCriticalSubtle
            TagVoice.Warning -> LocalColors.current.background.bgCautionSubtle
            TagVoice.Info -> LocalColors.current.background.bgInfoSubtle
            TagVoice.Positive -> LocalColors.current.background.bgPositiveSubtle
        }
    }

private data class TagPreviewData(
    val voice: TagVoice,
    val withIcon: Boolean,
)

private class TagPreviewProvider :
    PreviewParameterProvider<TagPreviewData> {
    override val values: Sequence<TagPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<TagPreviewData> =
        buildList {
            listOf(true, false).forEach { withIcon ->
                TagVoice.entries.forEach { voice ->
                    add(
                        element = TagPreviewData(
                            voice = voice,
                            withIcon = withIcon,
                        ),
                    )
                }
            }
        }.asSequence()
}

@Suppress("UnusedPrivateMember")
@Composable
@LemonadePreview
private fun SymbolContainerPreview(
    @PreviewParameter(TagPreviewProvider::class)
    previewData: TagPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
        LemonadeUi.Tag(
            label = "Small content",
            icon = LemonadeIcons.Heart.takeIf { previewData.withIcon },
            voice = previewData.voice,
        )

        LemonadeUi.Tag(
            label = "Fake long message - it had to be longer than that",
            icon = LemonadeIcons.Heart.takeIf { previewData.withIcon },
            voice = previewData.voice,
            modifier = Modifier.requiredWidth(width = 100.dp),
        )
    }
}
