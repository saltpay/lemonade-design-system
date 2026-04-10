import SwiftUI
import CoreText

/// Handles font registration for the Lemonade Design System.
/// Call `LemonadeFonts.registerFonts()` at app startup to ensure fonts are available.
public enum LemonadeFonts {
    private static let _registerFontsOnce: Void = {
        let fontNames = [
            "Figtree-Regular",
            "Figtree-Medium",
            "Figtree-SemiBold"
        ]

        for fontName in fontNames {
            registerFont(named: fontName)
        }
    }()

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
        _ = _registerFontsOnce
    }

    private static func registerFont(named fontName: String) {
        // Try multiple paths to find the font file
        var fontURL: URL?

        // Try with Fonts subdirectory (SPM structure)
        fontURL = Bundle.lemonade.url(forResource: fontName, withExtension: "ttf", subdirectory: "Fonts")

        // Try without subdirectory (Xcode project structure)
        if fontURL == nil {
            fontURL = Bundle.lemonade.url(forResource: fontName, withExtension: "ttf")
        }

        guard let url = fontURL else {
            #if DEBUG
            print("Lemonade: Could not find font file: \(fontName).ttf")
            #endif
            return
        }

        guard let fontDataProvider = CGDataProvider(url: url as CFURL) else {
            #if DEBUG
            print("Lemonade: Could not create data provider for font: \(fontName)")
            #endif
            return
        }

        guard let font = CGFont(fontDataProvider) else {
            #if DEBUG
            print("Lemonade: Could not create CGFont for: \(fontName)")
            #endif
            return
        }

        var error: Unmanaged<CFError>?
        if !CTFontManagerRegisterGraphicsFont(font, &error) {
            // Font might already be registered, which is fine
            if let error = error?.takeRetainedValue() {
                let errorDescription = CFErrorCopyDescription(error)
                // Only print if it's not "already registered" error
                if let desc = errorDescription as String?, !desc.contains("already registered") {
                    #if DEBUG
                    print("Lemonade: Error registering font \(fontName): \(desc)")
                    #endif
                }
            }
        }
    }
}
