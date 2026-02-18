package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
public fun Modifier.shadowBorder(
    width: Dp,
    color: Color,
    shape: Shape,
): Modifier =
    composed {
        dropShadow(
            shape = shape,
            shadow = Shadow(
                spread = width,
                color = color,
                radius = 0.dp,
                offset = DpOffset(
                    x = 0.dp,
                    y = 0.dp,
                ),
            ),
        )
    }
