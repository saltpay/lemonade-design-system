package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetSampleDisplay() {
    var showBasicSheet by remember { mutableStateOf(false) }
    var showNoDragHandleSheet by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Basic Bottom Sheet
        BottomSheetSection(title = "Basic Bottom Sheet") {
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.Button(
                label = "Open Bottom Sheet",
                onClick = { showBasicSheet = true },
                variant = LemonadeButtonVariant.Secondary,
                size = LemonadeButtonSize.Medium,
            )
        }

        // Without Drag Handle
        BottomSheetSection(title = "Without Drag Handle") {
            LemonadeUi.Text(
                text = "This bottom sheet has no drag handle",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.Button(
                label = "Open Without Drag Handle",
                onClick = { showNoDragHandleSheet = true },
                variant = LemonadeButtonVariant.Secondary,
                size = LemonadeButtonSize.Medium,
            )
        }
    }

    // Basic Bottom Sheet
    LemonadeUi.BottomSheet(
        expanded = showBasicSheet,
        onDismissRequest = { showBasicSheet = false },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            modifier = Modifier
                .fillMaxWidth()
                .padding(LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.Text(
                text = "Bottom Sheet Title",
                textStyle = LemonadeTheme.typography.headingSmall,
            )
            LemonadeUi.Text(
                text = "This is an example bottom sheet with free-form content. Swipe down or tap the scrim to dismiss.",
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.Button(
                label = "Close",
                onClick = { showBasicSheet = false },
                variant = LemonadeButtonVariant.Primary,
                size = LemonadeButtonSize.Medium,
            )
            Spacer(
                modifier = Modifier.height(LemonadeTheme.spaces.spacing400),
            )
        }
    }

    // Without Drag Handle
    LemonadeUi.BottomSheet(
        expanded = showNoDragHandleSheet,
        onDismissRequest = { showNoDragHandleSheet = false },
        showDragHandle = false,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            modifier = Modifier
                .fillMaxWidth()
                .padding(LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.Text(
                text = "No Drag Handle",
                textStyle = LemonadeTheme.typography.headingSmall,
            )
            LemonadeUi.Text(
                text = "This bottom sheet has no drag handle visible. Tap the scrim or use the button to close.",
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.Button(
                label = "Close",
                onClick = { showNoDragHandleSheet = false },
                variant = LemonadeButtonVariant.Primary,
                size = LemonadeButtonSize.Medium,
            )
            Spacer(
                modifier = Modifier.height(LemonadeTheme.spaces.spacing400),
            )
        }
    }
}

@Composable
private fun BottomSheetSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )

        content()
    }
}
