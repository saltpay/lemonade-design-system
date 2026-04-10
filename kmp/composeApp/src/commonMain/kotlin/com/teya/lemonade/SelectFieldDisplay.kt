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
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons

@Suppress("LongMethod")
@Composable
internal fun SelectFieldDisplay() {
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
        SelectFieldSection(title = "Basic") {
            LemonadeUi.SelectField(
                onClick = { },
                selectedValue = "ABC",
                placeholderText = "Select an option",
            )
        }

        // With Label
        SelectFieldSection(title = "With Label") {
            LemonadeUi.SelectField(
                onClick = { },
                placeholderText = "Select a category",
                label = "Category",
                selectedValue = null,
            )
        }

        // Filled
        SelectFieldSection(title = "Filled") {
            LemonadeUi.SelectField(
                onClick = { },
                selectedValue = "English",
                label = "Language",
            )
        }

        // With Leading Icon
        SelectFieldSection(title = "With Leading Icon") {
            LemonadeUi.SelectField(
                onClick = { },
                selectedValue = "Favourites",
                label = "Collection",
                leadingContent = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        tint = LemonadeTheme.colors.content.contentSecondary,
                    )
                },
            )
        }

        // With Error
        SelectFieldSection(title = "With Error") {
            LemonadeUi.SelectField(
                onClick = { },
                placeholderText = "Select an option",
                label = "Required Field",
                errorMessage = "Please select an option",
                error = true,
                selectedValue = null,
            )
        }

        // With Support Text
        SelectFieldSection(title = "With Support Text") {
            LemonadeUi.SelectField(
                onClick = { },
                placeholderText = "Select a country",
                label = "Country",
                supportText = "Choose your country of residence",
                optionalIndicator = "Optional",
                selectedValue = null,
            )
        }

        // Disabled
        SelectFieldSection(title = "Disabled") {
            LemonadeUi.SelectField(
                onClick = { },
                selectedValue = "Locked value",
                label = "Disabled Field",
                enabled = false,
            )
        }
    }
}

@Composable
private fun SelectFieldSection(
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
