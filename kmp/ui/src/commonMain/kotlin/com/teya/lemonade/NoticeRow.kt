package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.NoticeRowVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * An inline notification/banner component that displays a message with a voice-based style.
 * Typically used to communicate important information to the user.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.NoticeRow(
 *     description = "Your payment was successful",
 *     title = "Success",
 *     voice = NoticeRowVoice.Positive,
 * )
 * ```
 *
 * @param description - [String] message to be displayed in the notice.
 * @param modifier - [Modifier] to be applied to the root container of the notice row.
 * @param title - optional [String] title to show above the description.
 * @param voice - [NoticeRowVoice] to define the tone of voice. This will effectively define
 *  color of the background alongside the icon and text tints. Defaults to [NoticeRowVoice.Neutral].
 * @param leadingIcon - optional [LemonadeIcons] to show at the start. Defaults to voice-based icon.
 * @param onDismiss - optional callback when the dismiss icon is clicked. If null, no dismiss icon is shown.
 */
@Composable
public fun LemonadeUi.NoticeRow(
    description: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    voice: NoticeRowVoice = NoticeRowVoice.Neutral,
    leadingIcon: LemonadeIcons? = voice.defaultIcon,
    onDismiss: (() -> Unit)? = null,
) {
    CoreNoticeRow(
        description = description,
        modifier = modifier,
        title = title,
        voice = voice,
        leadingIcon = leadingIcon,
        onDismiss = onDismiss,
    )
}

@Composable
private fun CoreNoticeRow(
    description: String,
    title: String?,
    voice: NoticeRowVoice,
    leadingIcon: LemonadeIcons?,
    onDismiss: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = voice.containerColor,
                shape = LocalShapes.current.radius300,
            )
            .padding(all = LocalSpaces.current.spacing300),
    ) {
        if (leadingIcon != null) {
            LemonadeUi.Icon(
                icon = leadingIcon,
                contentDescription = null,
                tint = voice.tintColor,
                size = LemonadeAssetSize.Small,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing50),
            modifier = Modifier.weight(weight = 1f),
        ) {
            if (title != null) {
                LemonadeUi.Text(
                    text = title,
                    color = voice.tintColor,
                    textStyle = LocalTypographies.current.bodySmallMedium,
                )
            }

            LemonadeUi.Text(
                text = description,
                color = voice.tintColor,
                textStyle = LocalTypographies.current.bodySmallRegular,
            )
        }

        if (onDismiss != null) {
            LemonadeUi.Icon(
                icon = LemonadeIcons.Times,
                contentDescription = "Dismiss",
                tint = voice.tintColor,
                size = LemonadeAssetSize.Small,
                modifier = Modifier.clickable(onClick = onDismiss),
            )
        }
    }
}

private val NoticeRowVoice.tintColor: Color
    @Composable get() {
        return when (this) {
            NoticeRowVoice.Neutral -> LocalColors.current.content.contentPrimary
            NoticeRowVoice.Info -> LocalColors.current.content.contentInfo
            NoticeRowVoice.Warning -> LocalColors.current.content.contentCaution
            NoticeRowVoice.Critical -> LocalColors.current.content.contentCritical
            NoticeRowVoice.Positive -> LocalColors.current.content.contentPositive
        }
    }

private val NoticeRowVoice.containerColor: Color
    @Composable get() {
        return when (this) {
            NoticeRowVoice.Neutral -> LocalColors.current.background.bgNeutralSubtle
            NoticeRowVoice.Info -> LocalColors.current.background.bgInfoSubtle
            NoticeRowVoice.Warning -> LocalColors.current.background.bgCautionSubtle
            NoticeRowVoice.Critical -> LocalColors.current.background.bgCriticalSubtle
            NoticeRowVoice.Positive -> LocalColors.current.background.bgPositiveSubtle
        }
    }

private val NoticeRowVoice.defaultIcon: LemonadeIcons
    get() {
        return when (this) {
            NoticeRowVoice.Info -> LemonadeIcons.CircleInfo
            NoticeRowVoice.Warning -> LemonadeIcons.TriangleAlert
            NoticeRowVoice.Critical -> LemonadeIcons.CircleAlert
            NoticeRowVoice.Positive -> LemonadeIcons.CircleCheck
            NoticeRowVoice.Neutral -> LemonadeIcons.CircleInfo
        }
    }

private data class NoticeRowPreviewData(
    val voice: NoticeRowVoice,
    val hasTitle: Boolean,
    val hasDismiss: Boolean,
)

private class NoticeRowPreviewProvider :
    PreviewParameterProvider<NoticeRowPreviewData> {
    override val values: Sequence<NoticeRowPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<NoticeRowPreviewData> {
        return buildList {
            listOf(true, false).forEach { hasTitle ->
                listOf(true, false).forEach { hasDismiss ->
                    NoticeRowVoice.entries.forEach { voice ->
                        add(
                            element = NoticeRowPreviewData(
                                voice = voice,
                                hasTitle = hasTitle,
                                hasDismiss = hasDismiss,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
    }
}

@Suppress("UnusedPrivateMember")
@Composable
@LemonadePreview
private fun NoticeRowPreview(
    @PreviewParameter(NoticeRowPreviewProvider::class)
    previewData: NoticeRowPreviewData,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
    ) {
        LemonadeUi.NoticeRow(
            description = "This is a notice message",
            title = "Notice Title".takeIf { previewData.hasTitle },
            voice = previewData.voice,
            onDismiss = {}.takeIf { previewData.hasDismiss },
        )

        LemonadeUi.NoticeRow(
            description = "This is a longer notice message that spans multiple lines to" +
                " demonstrate how the component handles text wrapping and longer content",
            title = "Long Content Notice".takeIf { previewData.hasTitle },
            voice = previewData.voice,
            onDismiss = {}.takeIf { previewData.hasDismiss },
        )
    }
}
