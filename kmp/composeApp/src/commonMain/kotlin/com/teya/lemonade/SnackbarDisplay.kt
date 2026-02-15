package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
internal fun SnackbarDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Basic
        SnackbarSection(title = "Basic") {
            LemonadeUi.Snackbar(
                message = "Item added to cart",
            )
        }

        // With Action
        SnackbarSection(title = "With Action") {
            var actionCount by remember { mutableStateOf(value = 0) }
            LemonadeUi.Snackbar(
                message = "Changes saved successfully",
                actionLabel = "Undo",
                onActionClick = {
                    actionCount++
                },
            )
            if (actionCount > 0) {
                LemonadeUi.Text(
                    text = "Action clicked $actionCount times",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentTertiary,
                    modifier = Modifier.padding(top = LemonadeTheme.spaces.spacing200),
                )
            }
        }

        // Long Message
        SnackbarSection(title = "Long Message") {
            LemonadeUi.Snackbar(
                message = "Your payment has been processed successfully and a receipt has been sent to your email",
                actionLabel = "View",
                onActionClick = { },
            )
        }

        // Error
        SnackbarSection(title = "Error Message") {
            LemonadeUi.Snackbar(
                message = "Connection failed. Please try again",
                actionLabel = "Retry",
                onActionClick = { },
            )
        }

        // Simple Notification
        SnackbarSection(title = "Simple Notification") {
            LemonadeUi.Snackbar(
                message = "Settings updated",
            )
        }
    }
}

@Composable
private fun SnackbarSection(
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
