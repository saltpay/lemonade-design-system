import SwiftUI
import CoreText

/// Handles font registration for the Lemonade Design System.
/// Call `LemonadeFonts.registerFonts()` at app startup to ensure fonts are available.
public enum LemonadeFonts {
    private static var fontsRegistered = false

    /// The bundle containing Lemonade resources
    private static var lemonadeBundle: Bundle {
        #if SWIFT_PACKAGE
        return Bundle.module
        #else
        return Bundle(for: LemonadeBundleClass.self)
        #endif
    }

    /// Registers all Lemonade custom fonts from the bundle.
    /// This should be called once at app startup, typically in the App's init.
    ///
    /// ## Usage
    /// ```swift
    /// @main
    /// struct MyApp: App {
    ///     init() {
    ///         LemonadeFonts.registerFonts()
    ///     }
    ///     // ...
    /// }
    /// ```
    public static func registerFonts() {
        guard !fontsRegistered else { return }

        let fontNames = [
            "Figtree-Regular",
            "Figtree-Medium",
            "Figtree-SemiBold"
        ]

        for fontName in fontNames {
            registerFont(named: fontName)
        }

        fontsRegistered = true
    }

    private static func registerFont(named fontName: String) {
        // Try multiple paths to find the font file
        var fontURL: URL?

        // Try with Fonts subdirectory (SPM structure)
        fontURL = lemonadeBundle.url(forResource: fontName, withExtension: "ttf", subdirectory: "Fonts")

        // Try without subdirectory (Xcode project structure)
        if fontURL == nil {
            fontURL = lemonadeBundle.url(forResource: fontName, withExtension: "ttf")
        }

        guard let url = fontURL else {
            print("Lemonade: Could not find font file: \(fontName).ttf")
            return
        }

        guard let fontDataProvider = CGDataProvider(url: url as CFURL) else {
            print("Lemonade: Could not create data provider for font: \(fontName)")
            return
        }

        guard let font = CGFont(fontDataProvider) else {
            print("Lemonade: Could not create CGFont for: \(fontName)")
            return
        }

        var error: Unmanaged<CFError>?
        if !CTFontManagerRegisterGraphicsFont(font, &error) {
            // Font might already be registered, which is fine
            if let error = error?.takeRetainedValue() {
                let errorDescription = CFErrorCopyDescription(error)
                // Only print if it's not "already registered" error
                if let desc = errorDescription as String?, !desc.contains("already registered") {
                    print("Lemonade: Error registering font \(fontName): \(desc)")
                }
            }
        }
    }
}

// Helper class to get the framework bundle
private class LemonadeBundleClass {}
