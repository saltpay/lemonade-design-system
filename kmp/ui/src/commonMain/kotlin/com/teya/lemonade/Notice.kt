package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.NoticeVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A banner used to display brief, important messages within content.
 * Can include an icon and action to draw attention to contextual information or status updates.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Notice(
 *     content = "Your payment was processed successfully.",
 *     voice = NoticeVoice.Positive,
 * )
 *
 * LemonadeUi.Notice(
 *     title = "Action required",
 *     content = "Please update your billing information.",
 *     voice = NoticeVoice.Warning,
 *     actionLabel = "Update",
 *     onActionClick = { /* handle action */ },
 * )
 * ```
 *
 * @param content The body text displayed in the notice.
 * @param modifier [Modifier] applied to the root container.
 * @param title Optional bold heading displayed above the content.
 * @param voice [NoticeVoice] defining the semantic tone. Controls background, icon tint,
 *  and action text colors. Defaults to [NoticeVoice.Info].
 * @param icon Optional [LemonadeIcons] for the leading icon. When not specified,
 *  a default icon is chosen based on [voice]. Pass explicit `null` after setting
 *  this parameter to hide the icon entirely — use [showIcon] = false for that.
 * @param showIcon Whether to display the leading icon. Defaults to `true`.
 * @param actionLabel Optional text for the action button below the content.
 * @param onActionClick Callback invoked when the action is tapped.
 */
@Composable
public fun LemonadeUi.Notice(
    content: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    voice: NoticeVoice = NoticeVoice.Info,
    icon: LemonadeIcons? = voice.defaultIcon,
    showIcon: Boolean = true,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    CoreNotice(
        content = content,
        title = title,
        voice = voice,
        icon = icon.takeIf { showIcon },
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        modifier = modifier,
    )
}

@Composable
private fun CoreNotice(
    content: String,
    title: String?,
    voice: NoticeVoice,
    icon: LemonadeIcons?,
    actionLabel: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val colors = voice.noticeColors

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colors.containerColor,
                shape = LocalShapes.current.radius500,
            ).padding(all = LocalSpaces.current.spacing400),
    ) {
        if (icon != null) {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = null,
                tint = colors.iconTintColor,
                size = LemonadeAssetSize.Small,
                modifier = Modifier.padding(top = LocalSpaces.current.spacing50),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
            modifier = Modifier.weight(weight = 1f),
        ) {
            if (title != null) {
                LemonadeUi.Text(
                    text = title,
                    color = LocalColors.current.content.contentPrimary,
                    textStyle = LocalTypographies.current.bodyMediumSemiBold,
                )
            }

            LemonadeUi.Text(
                text = content,
                color = LocalColors.current.content.contentPrimary,
                textStyle = if (title != null) {
                    LocalTypographies.current.bodySmallRegular
                } else {
                    LocalTypographies.current.bodyMediumRegular
                },
            )

            if (actionLabel != null) {
                LemonadeUi.Text(
                    text = actionLabel,
                    color = colors.actionTextColor,
                    textStyle = LocalTypographies.current.bodyMediumSemiBold,
                    modifier = Modifier
                        .padding(top = LocalSpaces.current.spacing200)
                        .then(
                            if (onActionClick != null) {
                                Modifier.clickable(
                                    onClick = onActionClick,
                                    role = Role.Button,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalEffects.current.interactionIndication,
                                )
                            } else {
                                Modifier
                            },
                        ),
                )
            }
        }
    }
}

// region Voice → Token Mapping

@Stable
private data class NoticeColors(
    val containerColor: Color,
    val iconTintColor: Color,
    val actionTextColor: Color,
)

private val NoticeVoice.noticeColors: NoticeColors
    @Composable get() {
        return when (this) {
            NoticeVoice.Info -> NoticeColors(
                containerColor = LocalColors.current.background.bgInfoSubtle,
                iconTintColor = LocalColors.current.content.contentInfo,
                actionTextColor = LocalColors.current.content.contentInfo,
            )
            NoticeVoice.Positive -> NoticeColors(
                containerColor = LocalColors.current.background.bgPositiveSubtle,
                iconTintColor = LocalColors.current.content.contentPositive,
                actionTextColor = LocalColors.current.content.contentPositive,
            )
            NoticeVoice.Warning -> NoticeColors(
                containerColor = LocalColors.current.background.bgCautionSubtle,
                iconTintColor = LocalColors.current.content.contentCaution,
                actionTextColor = LocalColors.current.content.contentCaution,
            )
            NoticeVoice.Critical -> NoticeColors(
                containerColor = LocalColors.current.background.bgCriticalSubtle,
                iconTintColor = LocalColors.current.content.contentCritical,
                actionTextColor = LocalColors.current.content.contentCritical,
            )
            NoticeVoice.Neutral -> NoticeColors(
                containerColor = LocalColors.current.background.bgElevated,
                iconTintColor = LocalColors.current.content.contentSecondary,
                actionTextColor = LocalColors.current.content.contentNeutral,
            )
        }
    }

private val NoticeVoice.defaultIcon: LemonadeIcons
    get() = when (this) {
        NoticeVoice.Info -> LemonadeIcons.CircleInfo
        NoticeVoice.Positive -> LemonadeIcons.CircleCheck
        NoticeVoice.Warning -> LemonadeIcons.TriangleAlert
        NoticeVoice.Critical -> LemonadeIcons.TriangleAlert
        NoticeVoice.Neutral -> LemonadeIcons.Heart
    }

// endregion

// region Previews

private data class NoticePreviewData(
    val voice: NoticeVoice,
    val withTitle: Boolean,
    val withAction: Boolean,
    val withIcon: Boolean,
)

private class NoticePreviewProvider : PreviewParameterProvider<NoticePreviewData> {
    override val values: Sequence<NoticePreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<NoticePreviewData> =
        buildList {
            NoticeVoice.entries.forEach { voice ->
                listOf(true, false).forEach { withTitle ->
                    listOf(true, false).forEach { withAction ->
                        listOf(true, false).forEach { withIcon ->
                            add(
                                element = NoticePreviewData(
                                    voice = voice,
                                    withTitle = withTitle,
                                    withAction = withAction,
                                    withIcon = withIcon,
                                ),
                            )
                        }
                    }
                }
            }
        }.asSequence()
}

@LemonadePreview
@Composable
private fun NoticePreview(
    @PreviewParameter(NoticePreviewProvider::class)
    previewData: NoticePreviewData,
) {
    LemonadeUi.Notice(
        content = "This is a notice message with important information.",
        title = "Notice Title".takeIf { previewData.withTitle },
        voice = previewData.voice,
        showIcon = previewData.withIcon,
        actionLabel = "Action".takeIf { previewData.withAction },
        onActionClick = {},
    )
}

// endregion
