package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

@Suppress("LongMethod")
@Composable
internal fun TextFieldDisplay() {
    var basicText by remember { mutableStateOf("") }
    var labeledText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("Invalid input") }
    var supportText by remember { mutableStateOf("") }
    var leadingText by remember { mutableStateOf("") }
    var trailingText by remember { mutableStateOf("") }
    var selectorText by remember { mutableStateOf("") }

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
        TextFieldSection(title = "Basic") {
            LemonadeUi.TextField(
                input = basicText,
                onInputChanged = { basicText = it },
                placeholderText = "Enter text...",
            )
        }

        // With Label
        TextFieldSection(title = "With Label") {
            LemonadeUi.TextField(
                input = labeledText,
                onInputChanged = { labeledText = it },
                label = "Email Address",
                placeholderText = "you@example.com",
            )
        }

        // With Error
        TextFieldSection(title = "With Error") {
            LemonadeUi.TextField(
                input = errorText,
                onInputChanged = { errorText = it },
                label = "Username",
                placeholderText = "Enter username",
                errorMessage = "Username is already taken",
                error = true,
            )
        }

        // With Support Text
        TextFieldSection(title = "With Support Text") {
            LemonadeUi.TextField(
                input = supportText,
                onInputChanged = { supportText = it },
                label = "Password",
                supportText = "Must be at least 8 characters",
                placeholderText = "Enter password",
            )
        }

        // With Leading Icon
        TextFieldSection(title = "With Leading Icon") {
            LemonadeUi.TextField(
                input = leadingText,
                onInputChanged = { leadingText = it },
                label = "Search",
                placeholderText = "Search...",
                leadingContent = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Search,
                        contentDescription = null,
                        tint = LemonadeTheme.colors.content.contentSecondary,
                    )
                },
            )
        }

        // With Trailing Icon
        TextFieldSection(title = "With Trailing Icon") {
            LemonadeUi.TextField(
                input = trailingText,
                onInputChanged = { trailingText = it },
                label = "Amount",
                placeholderText = "0.00",
                trailingContent = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.CircleInfo,
                        contentDescription = null,
                        tint = LemonadeTheme.colors.content.contentSecondary,
                    )
                },
            )
        }

        // TextField With Selector
        TextFieldSection(title = "TextField With Selector") {
            LemonadeUi.TextFieldWithSelector(
                input = selectorText,
                onInputChanged = { selectorText = it },
                leadingAction = { println("Show country code picker") },
                leadingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing100),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LemonadeUi.Text(
                            text = "+1",
                            textStyle = LemonadeTheme.typography.bodyMediumMedium,
                        )
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.ChevronDown,
                            contentDescription = null,
                            size = LemonadeAssetSize.Small,
                        )
                    }
                },
                label = "Phone Number",
                placeholderText = "Enter phone number",
            )
        }

        // Disabled
        TextFieldSection(title = "Disabled") {
            LemonadeUi.TextField(
                input = "Disabled content",
                onInputChanged = {},
                label = "Disabled Field",
                placeholderText = "Cannot edit",
                enabled = false,
            )
        }
    }
}

@Composable
private fun TextFieldSection(
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
