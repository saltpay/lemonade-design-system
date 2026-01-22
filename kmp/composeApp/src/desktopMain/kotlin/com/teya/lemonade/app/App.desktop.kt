package com.teya.lemonade.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.Display

@Composable
internal actual fun App() {
    var currentScreenStack: Set<Display> by remember {
        mutableStateOf(setOf(Display.Home))
    }

    Row {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(weight = 1f),
        ) {
            screens[Display.Home]?.invoke { focusScreen ->
                currentScreenStack = setOf(Display.Home, focusScreen)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(weight = 4f),
        ) {
            currentScreenStack.elementAtOrNull(1)
                ?.let { focusScreen ->
                    screens[focusScreen]?.invoke { replaceScreen ->
                        currentScreenStack = setOf(Display.Home, replaceScreen)
                    }
                }
        }
    }
}