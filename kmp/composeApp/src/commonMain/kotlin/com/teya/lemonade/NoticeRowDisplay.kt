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
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.NoticeRowVoice

@Composable
internal fun NoticeRowDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        VoicesSection()
        WithTitlesSection()
        WithDismissSection()
        CustomIconsSection()
        WithoutIconsSection()
        UseCasesSection()
    }
}

@Composable
private fun VoicesSection() {
    NoticeRowSection(title = "Voices") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            LemonadeUi.NoticeRow(
                description = "This is a neutral notice message",
                voice = NoticeRowVoice.Neutral,
            )
            LemonadeUi.NoticeRow(
                description = "This is an informational notice message",
                voice = NoticeRowVoice.Info,
            )
            LemonadeUi.NoticeRow(
                description = "This is a warning notice message",
                voice = NoticeRowVoice.Warning,
            )
            LemonadeUi.NoticeRow(
                description = "This is a critical notice message",
                voice = NoticeRowVoice.Critical,
            )
            LemonadeUi.NoticeRow(
                description = "This is a positive notice message",
                voice = NoticeRowVoice.Positive,
            )
        }
    }
}

@Composable
private fun WithTitlesSection() {
    NoticeRowSection(title = "With Titles") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            LemonadeUi.NoticeRow(
                title = "Information",
                description = "Your account has been updated successfully",
                voice = NoticeRowVoice.Info,
            )
            LemonadeUi.NoticeRow(
                title = "Warning",
                description = "Your session will expire in 5 minutes",
                voice = NoticeRowVoice.Warning,
            )
            LemonadeUi.NoticeRow(
                title = "Error",
                description = "Unable to process your request. Please try again",
                voice = NoticeRowVoice.Critical,
            )
            LemonadeUi.NoticeRow(
                title = "Success",
                description = "Your payment was processed successfully",
                voice = NoticeRowVoice.Positive,
            )
        }
    }
}

@Composable
private fun WithDismissSection() {
    var showNotice1 by remember { mutableStateOf(value = true) }
    var showNotice2 by remember { mutableStateOf(value = true) }
    var showNotice3 by remember { mutableStateOf(value = true) }

    NoticeRowSection(title = "With Dismiss") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            if (showNotice1) {
                LemonadeUi.NoticeRow(
                    title = "Dismissible Notice",
                    description = "Click the X to dismiss this notice",
                    voice = NoticeRowVoice.Info,
                    onDismiss = { showNotice1 = false },
                )
            }
            if (showNotice2) {
                LemonadeUi.NoticeRow(
                    description = "This warning can be dismissed",
                    voice = NoticeRowVoice.Warning,
                    onDismiss = { showNotice2 = false },
                )
            }
            if (showNotice3) {
                LemonadeUi.NoticeRow(
                    title = "Success",
                    description = "Operation completed successfully",
                    voice = NoticeRowVoice.Positive,
                    onDismiss = { showNotice3 = false },
                )
            }
        }
    }
}

@Composable
private fun CustomIconsSection() {
    NoticeRowSection(title = "Custom Icons") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            LemonadeUi.NoticeRow(
                title = "Payment Required",
                description = "Please update your payment method",
                voice = NoticeRowVoice.Warning,
                leadingIcon = LemonadeIcons.MoneyDollar,
            )
            LemonadeUi.NoticeRow(
                title = "Security Alert",
                description = "Enable two-factor authentication for better security",
                voice = NoticeRowVoice.Info,
                leadingIcon = LemonadeIcons.Shield,
            )
            LemonadeUi.NoticeRow(
                title = "Locked",
                description = "This feature is locked. Upgrade to unlock",
                voice = NoticeRowVoice.Neutral,
                leadingIcon = LemonadeIcons.Padlock,
            )
        }
    }
}

@Composable
private fun WithoutIconsSection() {
    NoticeRowSection(title = "Without Icons") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            LemonadeUi.NoticeRow(
                title = "System Maintenance",
                description = "Scheduled maintenance will occur on Saturday",
                voice = NoticeRowVoice.Warning,
                leadingIcon = null,
            )
            LemonadeUi.NoticeRow(
                description = "New features are now available in your dashboard",
                voice = NoticeRowVoice.Info,
                leadingIcon = null,
            )
        }
    }
}

@Composable
private fun UseCasesSection() {
    NoticeRowSection(title = "Use Cases") {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        ) {
            LemonadeUi.NoticeRow(
                title = "Form Error",
                description = "Please fill in all required fields before submitting",
                voice = NoticeRowVoice.Critical,
            )
            LemonadeUi.NoticeRow(
                description = "A new version of the app is available",
                voice = NoticeRowVoice.Info,
                leadingIcon = LemonadeIcons.Sparkles,
            )
            LemonadeUi.NoticeRow(
                title = "Upload Complete",
                description = "Your files have been uploaded successfully",
                voice = NoticeRowVoice.Positive,
                leadingIcon = LemonadeIcons.CircleCheck,
                onDismiss = {},
            )
            LemonadeUi.NoticeRow(
                title = "Terms Update",
                description = "Our terms of service have been updated." +
                    " Please review the changes.",
                voice = NoticeRowVoice.Neutral,
            )
        }
    }
}

@Composable
private fun NoticeRowSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
