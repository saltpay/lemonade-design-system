package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant

@Composable
private fun HiddenNavBarBottomSheetSample() {
    var showSheet by remember { mutableStateOf(false) }

    LemonadeUi.Text(
        text = "Hides the system navigation bar inside the sheet dialog (Android only)",
        textStyle = LemonadeTheme.typography.bodySmallRegular,
        color = LemonadeTheme.colors.content.contentSecondary,
    )
    LemonadeUi.Button(
        label = "Open Without Navigation Bar",
        onClick = { showSheet = true },
        variant = LemonadeButtonVariant.Secondary,
        size = LemonadeButtonSize.Medium,
    )

    LemonadeUi.BottomSheet(
        expanded = showSheet,
        onDismissRequest = { showSheet = false },
        hideNavigationBar = true,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = LemonadeTheme.spaces.spacing400,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.Text(
                text = "Hidden Navigation Bar",
                textStyle = LemonadeTheme.typography.headingSmall,
            )
            LemonadeUi.Text(
                text = "The system navigation bar is hidden in this bottom sheet. Swipe up from the bottom edge to temporarily reveal it.",
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            LemonadeUi.Button(
                label = "Close",
                onClick = { showSheet = false },
                variant = LemonadeButtonVariant.Primary,
                size = LemonadeButtonSize.Medium,
            )
            Spacer(
                modifier = Modifier.height(
                    height = LemonadeTheme.spaces.spacing400,
                ),
            )
        }
    }
}
