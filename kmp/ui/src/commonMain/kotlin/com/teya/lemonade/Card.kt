package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.TagVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
public fun LemonadeUi.Card(
    modifier: Modifier = Modifier,
    contentPadding: LemonadeCardPadding = LemonadeCardPadding.None,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    content: (@Composable ColumnScope.() -> Unit),
) {
    CoreCard(
        modifier = modifier,
        contentPadding = contentPadding,
        background = background,
        header = header,
        content = content
    )
}

@Composable
private fun CoreCard(
    modifier: Modifier = Modifier,
    contentPadding: LemonadeCardPadding,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val hasHeader = header !== null
    val headerConfig = if (hasHeader) {
        CardHeaderConfig(
            title = header.title,
            trailingSlot = header.trailingSlot
        )
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(LocalRadius.current.radius400))
            .background(color = background.background)
    ) {
        if (hasHeader) {
            CardHeader(config = headerConfig)
        }

        Column(
            modifier = Modifier
                .padding(contentPadding.spacing)
        ) {
            content()
        }
    }
}

public data class CardHeaderConfig(
    val title: String,
    val trailingSlot: (@Composable RowScope.() -> Unit)? = null
)

@Composable
private fun CardHeader(
    modifier: Modifier = Modifier,
    config: CardHeaderConfig? = null,
) {
    if (config == null) return

    val (title, trailingSlot) = config

    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing200),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = LocalSpaces.current.spacing400,
                top = LocalSpaces.current.spacing400,
                end = LocalSpaces.current.spacing400,
                bottom = LocalSpaces.current.spacing0,
            )
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LocalTypographies.current.headingXXSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1F),
        )


        if (trailingSlot !== null) {
            trailingSlot()
        }
    }
}

private val LemonadeCardPadding.spacing: Dp
    @Composable get() = when (this) {
        LemonadeCardPadding.None -> LocalSpaces.current.spacing0
        LemonadeCardPadding.XSmall -> LocalSpaces.current.spacing100
        LemonadeCardPadding.Small -> LocalSpaces.current.spacing200
        LemonadeCardPadding.Medium -> LocalSpaces.current.spacing400
    }

private val LemonadeCardBackground.background: Color
    @Composable get() = when (this) {
        LemonadeCardBackground.Default -> LocalColors.current.background.bgDefault
        LemonadeCardBackground.Subtle -> LocalColors.current.background.bgSubtle
    }

private data class CardPreviewData(
    val background: LemonadeCardBackground,
    val contentPadding: LemonadeCardPadding,
    var header: CardHeaderConfig?
)

private class CardPreviewProvider : PreviewParameterProvider<CardPreviewData> {
    override val values: Sequence<CardPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<CardPreviewData> {
        return buildList {
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
                                    }
                                ).takeIf { withHeader }
                            )
                        )
                    }
                }
            }
        }.asSequence()
    }
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
                    .height(LemonadeTheme.sizes.size1600)
                    .background(LemonadePrimitiveColors.Alpha.Pink.alpha200)
            ) {
                LemonadeUi.Text(
                    text = "Background: ${previewData.background} • Spacing: ${previewData.contentPadding}",
                    textStyle = LemonadeTheme.typography.bodySmallMedium,
                    color = LemonadeTheme.colors.content.contentSecondary
                )
            }
        }
    )
}