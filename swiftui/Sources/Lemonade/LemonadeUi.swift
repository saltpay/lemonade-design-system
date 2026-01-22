import SwiftUI

/// LemonadeUi is a struct that serves as a marker for the Lemonade UI module.
/// It exposes the Lemonade SwiftUI components to the rest of the application.
///
/// Usage:
/// ```swift
/// LemonadeUi.Text("Hello, World!")
/// LemonadeUi.Button(label: "Click me") { print("Clicked") }
/// LemonadeUi.Icon(icon: .check, contentDescription: "Checkmark")
/// ```
public struct LemonadeUi {
    private init() {}
}
