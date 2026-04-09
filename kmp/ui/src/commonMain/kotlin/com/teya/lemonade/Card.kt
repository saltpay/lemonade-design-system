package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.LemonadeCardHeadingStyle
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.TagVoice

@Composable
public fun LemonadeUi.Card(
    modifier: Modifier = Modifier,
    contentPadding: LemonadeCardPadding = LemonadeCardPadding.None,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    footerAction: CardFooterActionConfig? = null,
    content: (@Composable ColumnScope.() -> Unit),
) {
    CoreCard(
        modifier = modifier,
        contentPadding = contentPadding,
        background = background,
        header = header,
        footerAction = footerAction,
        content = content,
    )
}

@Composable
private fun CoreCard(
    modifier: Modifier = Modifier,
    contentPadding: LemonadeCardPadding,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    footerAction: CardFooterActionConfig? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = LocalShapes.current.semantic.radiusContainerDefault)
            .background(color = background.background),
    ) {
        if (header != null) {
            CardHeader(config = header)
        }

        Column(
            modifier = Modifier
                .padding(contentPadding.spacing),
        ) {
            content()
        }

        if (footerAction != null) {
            CardFooterAction(config = footerAction)
        }
    }
}

public data class CardHeaderConfig(
    val title: String,
    val headingStyle: LemonadeCardHeadingStyle = LemonadeCardHeadingStyle.Default,
    val leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    val trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    val showNavigationIndicator: Boolean = false,
)

@Composable
private fun CardHeader(
    modifier: Modifier = Modifier,
    config: CardHeaderConfig? = null,
) {
    if (config == null) return

    val titleTextStyle = config.headingStyle.textStyle
    val titleColor = config.headingStyle.color

    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing200),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = LocalSpaces.current.spacing400,
                top = LocalSpaces.current.spacing400,
                end = LocalSpaces.current.spacing400,
                bottom = LocalSpaces.current.spacing0,
            ),
    ) {
        if (config.leadingSlot != null) {
            config.leadingSlot.invoke(this)
        }

        LemonadeUi.Text(
            text = config.title,
            textStyle = titleTextStyle,
            color = titleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1F),
        )

        if (config.trailingSlot != null) {
            config.trailingSlot.invoke(this)
        }

        if (config.showNavigationIndicator) {
            LemonadeUi.Icon(
                icon = LemonadeIcons.ChevronRight,
                contentDescription = null,
                size = LemonadeAssetSize.Medium,
                tint = LocalColors.current.content.contentSecondary,
            )
        }
    }
}

public data class CardFooterActionConfig(
    val label: String,
    val onClick: () -> Unit,
)

@Composable
private fun CardFooterAction(
    config: CardFooterActionConfig,
) {
    LemonadeUi.HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = config.onClick)
            .padding(
                horizontal = LocalSpaces.current.spacing400,
                vertical = LocalSpaces.current.spacing200,
            )
            .padding(bottom = LocalSpaces.current.spacing200),
    ) {
        LemonadeUi.Text(
            text = config.label,
            textStyle = LocalTypographies.current.bodySmallSemiBold,
            color = LocalColors.current.content.contentPrimary,
        )
    }
}

private val LemonadeCardHeadingStyle.textStyle: LemonadeTextStyle
    @Composable get() {
        return when (this) {
            LemonadeCardHeadingStyle.Default -> LocalTypographies.current.headingXXSmall
            LemonadeCardHeadingStyle.Overline -> LocalTypographies.current.bodyXSmallOverline
        }
    }

private val LemonadeCardHeadingStyle.color: Color
    @Composable get() {
        return when (this) {
            LemonadeCardHeadingStyle.Default -> LocalColors.current.content.contentPrimary
            LemonadeCardHeadingStyle.Overline -> LocalColors.current.content.contentSecondary
        }
    }

private val LemonadeCardPadding.spacing: Dp
    @Composable get() {
        return when (this) {
            LemonadeCardPadding.None -> LocalSpaces.current.spacing0
            LemonadeCardPadding.XSmall -> LocalSpaces.current.spacing100
            LemonadeCardPadding.Small -> LocalSpaces.current.spacing200
            LemonadeCardPadding.Medium -> LocalSpaces.current.spacing400
        }
    }

private val LemonadeCardBackground.background: Color
    @Composable get() {
        return when (this) {
            LemonadeCardBackground.Default -> LocalColors.current.background.bgDefault
            LemonadeCardBackground.Subtle -> LocalColors.current.background.bgSubtle
            LemonadeCardBackground.Elevated -> LocalColors.current.background.bgElevated
        }
    }

@Suppress("DataClassShouldBeImmutable")
private data class CardPreviewData(
    val background: LemonadeCardBackground,
    val contentPadding: LemonadeCardPadding,
    var header: CardHeaderConfig?,
)

private class CardPreviewProvider : PreviewParameterProvider<CardPreviewData> {
    override val values: Sequence<CardPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<CardPreviewData> =
        buildList {
            LemonadeCardPadding.entries.forEach { contentPadding ->
                LemonadeCardBackground.entries.forEach { background ->
                    listOf(true, false).forEach { withHeader ->
                        add(
                            CardPreviewData(
                                background = background,
                                contentPadding = contentPadding,
                                header = CardHeaderConfig(
                                    title = "Card heading",
                                    trailingSlot = {
                                        LemonadeUi.Tag("Tag label", voice = TagVoice.Neutral)
                                    },
                                ).takeIf { withHeader },
                            ),
                        )
                    }
                }
            }
        }.asSequence()
}

@OptIn(InternalLemonadeApi::class)
@LemonadePreview
@Composable
private fun CardPreview(
    @PreviewParameter(CardPreviewProvider::class)
    previewData: CardPreviewData,
) {
    LemonadeUi.Card(
        background = previewData.background,
        header = previewData.header,
        contentPadding = previewData.contentPadding,
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalSizes.current.size1600)
                    .background(LemonadePrimitiveColors.Alpha.Pink.alpha200),
            ) {
                LemonadeUi.Text(
                    text = "Background: ${previewData.background} • Spacing: ${previewData.contentPadding}",
                    textStyle = LocalTypographies.current.bodySmallMedium,
                    color = LocalColors.current.content.contentSecondary,
                )
            }
        },
    )
}
