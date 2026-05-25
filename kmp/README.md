# Lemonade Design System - Kotlin Multiplatform

A shared **Kotlin Multiplatform** library for UI components, styling, and theming — enabling consistent user experiences across Android, iOS, and JVM Desktop.

---

## Table of Contents

- [Overview](#overview)
- [Supported Platforms](#supported-platforms)
- [Getting Started](#getting-started)
  - [Step 1: Add to libs.toml](#step-1-add-to-libstoml)
  - [Step 2: Apply in build.gradle.kts](#step-2-apply-in-buildgradlekts)
- [Configuring the Theme](#configuring-the-theme)
- [Using Components](#using-components)
- [Components](#components)
- [Design Tokens](#design-tokens)
- [Assets](#assets)
- [Screenshot Testing](#screenshot-testing)
- [Contributing](#contributing)
  - [Documentation Standards](#documentation-standards)

---

## Overview

This guide provides the essential steps to integrate the Lemonade Design System KMP library into your Android, iOS, or JVM application.

By the end of this guide, you will have:

- Added the library dependency
- Wrapped your application in the core theme provider
- Used a basic component from the library

---

## Supported Platforms

| Platform | Target |
|----------|--------|
| Android | Mobile |
| iOS | Mobile |
| JVM | Desktop |

---

## Getting Started

You'll need to add the library to your project's build files. We recommend using the version catalog (`libs.toml`).

### Step 1: Add to `libs.toml`

Add the library to your `gradle/libs.toml` file. You can find the latest version by checking for tags with `lemonade-kmp-v*`.

```toml
[versions]
lemonade = "latest-version"

[libraries]
# Main UI library
lemonade-ui = { module = "com.teya.foundation:lemonade-ui", version.ref = "lemonade" }

# Core definitions (included in lemonade-ui)
# Contains core component definitions for server-driven UI support
lemonade-core = { module = "com.teya.foundation:lemonade-core", version.ref = "lemonade" }
```

### Step 2: Apply in `build.gradle.kts`

Add the library to the `commonMain` source set in your module-level `build.gradle.kts` (e.g., `shared/build.gradle.kts`):

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                // ... other dependencies

                // Add the design system library
                implementation(libs.lemonade.ui)
            }
        }
        // ... other source sets
    }
}
```

---

## Configuring the Theme

To make all the colors, typography, shapes, and other design tokens available to your app, wrap your content in the `LemonadeTheme` provider. This is **optional** — if not implemented, default values will be used for all Lemonade components.

This is typically done once at the root of your application, usually in your `commonMain` entry point (e.g., `App.kt`):

```kotlin
import com.teya.lemonade

@Composable
fun App() {
    LemonadeTheme(
        // All options are overrideable to adapt to your app's needs
        colors = LemonadeTheme.colors,
        typography = LemonadeTheme.typography,
        radius = LemonadeTheme.radius,
        shapes = LemonadeTheme.shapes,
        opacities = LemonadeTheme.opacities,
        spaces = LemonadeTheme.spaces,
        borderWidths = LemonadeTheme.borderWidths,
    ) {
        // Your application's content goes here
        MyScreenContent()
    }
}
```

---

## Using Components

For easy discovery, Lemonade DS provides a `LemonadeUi` object. Import it via `import com.teya.lemonade.LemonadeUi` — auto-complete will help you discover available components, and it makes it easy to distinguish where components are coming from.

### Basic Usage Example

Here is a simple example using `Switch` and `Text` from the library:

```kotlin
import com.teya.lemonade.LemonadeUi

@Composable
fun MyScreenContent() {
    var checked by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(LemonadeTheme.spaces.spacing100), // Using spacing tokens
    ) {
        LemonadeUi.Text(
            text = "Welcome to Lemonade - Are you loving it?",
        )

        LemonadeUi.Switch(
            checked = checked,
            onCheckedChange = { checked = !checked },
        )
    }
}
```

---

## Components

| Category | Examples |
|----------|----------|
| **Form Controls** | Switch, Input, Checkbox |
| **Display** | Text, Badge, Avatar |
| **Selection & Lists** | List, Dropdown |

---

## Design Tokens

| Token | Description |
|-------|-------------|
| **Colors** | Primitive and semantic color tokens |
| **Typography** | Figtree font family with multiple styles |
| **Spacing** | Consistent spacing tokens (`spaces.spacing100`, etc.) |
| **Radius** | Border radius tokens |
| **Shapes** | Pre-defined shape configurations |
| **Opacities** | Opacity level tokens |
| **Border Widths** | Border width tokens |

---

## Assets

| Asset Type | Description |
|------------|-------------|
| **BrandLogo** | Brand logo assets |
| **CountryFlag** | Country flag icons |
| **SVG Icons** | Comprehensive icon library |

---

## Screenshot Testing

The `:ui` module is covered by **Compose `@Preview` screenshot tests** (Roborazzi + Robolectric). Every `@Preview` is discovered automatically and captured in **both light and dark themes** — there is no per-preview boilerplate.

### Adding coverage

Just write a `@Preview` (or annotate with `@LemonadePreview`). The tester finds it, wraps it in `LemonadeTheme`, and records one golden per theme (`…_Light.png` / `…_Dark.png`). To shape what gets captured:

- **Light + dark** are always captured automatically — you do **not** need to declare `uiMode`.
- **Direction:** previews render in both LTR and RTL in the IDE, but only **LTR** is screenshot-tested.
- **Keep `@PreviewParameter` providers representative**, not combinatorial. Vary one axis at a time from a sensible base (every enum value once, each boolean's non-default once) rather than a full cartesian product — this keeps the golden set and the IDE preview grid small and meaningful.

### Goldens are authored by CI — don't commit them by hand

Golden images live in `kmp/ui/screenshots/` but are **recorded and committed by CI**, not by you. Pixel rasterisation is OS-dependent, so goldens are pinned to one authoritative environment: GitHub's `ubuntu-latest`.

- On a pull request, the **`Android Screenshots`** job runs `verifyRoborazziDebug`. If it fails (e.g. you added or changed a preview), CI re-records the goldens and pushes a `🤖 Update screenshots` commit back to your branch. Review the image diff inline in the PR.
- **Do not commit locally-recorded PNGs** — goldens recorded on macOS will not match the Linux CI renderer and will just churn. Let CI own them.
- Fork PRs and pushes to `main` can't be auto-committed; in those cases the job fails with instructions to record locally.

### Running it locally

From `kmp/`:

```bash
# Re-record goldens (writes to kmp/ui/screenshots/)
./gradlew :ui:recordRoborazziDebug

# Verify current code against the recorded goldens
./gradlew :ui:verifyRoborazziDebug
```

Useful for iterating locally, but **Linux CI is authoritative** — only the goldens CI commits are trusted. The regular `Android` build does not pay the Robolectric render cost; screenshots run only in the dedicated job.

---

## Contributing

### Documentation Standards

All public APIs must be thoroughly documented. Clear documentation helps other developers understand and use the components correctly.

#### Don't

Leave public APIs undocumented or with lazy comments:

```kotlin
// This is the switch
@Composable
public fun LemonadeUi.Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
) {
    // ...
}
```

#### Do

Write clear, comprehensive KDoc for all public APIs:

```kotlin
/**
 * This composable provides the fundamental visual and interactive elements of a toggle switch,
 * including the track and thumb. It handles animations for state changes like checked, enabled,
 * hover, and press. It is designed to be the internal building block for a higher-level,
 * public-facing switch component.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Switch(
 *     checked = true,
 *     onCheckedChange = { isChecked -> /* ... */ },
 * )
 * ```
 *
 * ## Parameters
 * @param checked `true` if the switch is in the "on" state, `false` otherwise.
 * @param onCheckedChange A callback invoked when the user interacts with the switch to change its state.
 * @param enabled Optional - controls the enabled state of the switch. When `false`, interaction is
 *   disabled and it is visually styled as such. Defaults to true.
 * @param interactionSource Optional [MutableInteractionSource] used to observe interaction states
 *   like hover and press to drive visual feedback.
 * @param modifier Optional [Modifier] to be applied to the root container of the switch.
 */
@Composable
public fun LemonadeUi.Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
) {
    // ...
}
```
