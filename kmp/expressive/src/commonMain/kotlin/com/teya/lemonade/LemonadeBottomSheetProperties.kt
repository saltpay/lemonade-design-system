package com.teya.lemonade

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Immutable

/**
 * Configuration of dismissal behaviours for [LemonadeUi.BottomSheet].
 *
 * Mirrors [androidx.compose.material3.ModalBottomSheetProperties] but keeps the public surface of
 * the Lemonade Design System free of Material 3 types, matching the naming used by
 * [LemonadeUi.Dialog].
 *
 * @param dismissOnBackPress Whether pressing the back button calls `onDismissRequest`. Defaults
 *   to `true`.
 * @param dismissOnClickOutside Whether tapping the scrim calls `onDismissRequest`. Defaults to
 *   `true`.
 */
@Immutable
public class LemonadeBottomSheetProperties(
    public val dismissOnBackPress: Boolean = true,
    public val dismissOnClickOutside: Boolean = true,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LemonadeBottomSheetProperties) return false
        if (dismissOnBackPress != other.dismissOnBackPress) return false
        if (dismissOnClickOutside != other.dismissOnClickOutside) return false
        return true
    }

    override fun hashCode(): Int {
        var result = dismissOnBackPress.hashCode()
        result = 31 * result + dismissOnClickOutside.hashCode()
        return result
    }
}

@OptIn(ExperimentalMaterial3Api::class)
internal fun LemonadeBottomSheetProperties.toMaterial(): ModalBottomSheetProperties = ModalBottomSheetProperties(
    shouldDismissOnBackPress = dismissOnBackPress,
    shouldDismissOnClickOutside = dismissOnClickOutside,
)
