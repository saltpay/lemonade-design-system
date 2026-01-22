package com.teya.lemonade

import androidx.compose.ui.window.ComposeUIViewController
import com.teya.lemonade.app.App
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController = ComposeUIViewController { App() }
