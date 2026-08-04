import SwiftUI
import Lemonade

@main
struct LemonadeSampleApp: App {
    @StateObject private var styleHandler = LemonadeStyleHandler()

    init() {
        // Register Lemonade fonts at app startup.
        // `registerFonts()` deliberately swallows failures, so never force-unwrap
        // the result of `UIFont(name:size:)` here - a resource-bundling regression
        // would turn a cosmetic problem into a launch crash.
        LemonadeFonts.registerFonts()

        // Customize the font for large titles
        UINavigationBar.appearance().largeTitleTextAttributes = [
            .font: Self.figtreeSemibold(size: LemonadeTypography.shared.headingLarge.fontSize)
        ]

        // Customize the font for inline titles
        UINavigationBar.appearance().titleTextAttributes = [
            .font: Self.figtreeSemibold(size: LemonadeTypography.shared.headingXXSmall.fontSize)
        ]

        // Customize the font for the search bar
        UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).font = UIFont(
            name: "Figtree",
            size: LemonadeTypography.shared.bodyMediumMedium.fontSize
        )
    }

    /// The Figtree semibold face, falling back to the system font at the same
    /// size and weight if the custom font failed to register.
    private static func figtreeSemibold(size: CGFloat) -> UIFont {
        if let font = UIFont(name: "Figtree-Semibold", size: size) {
            return font
        }
        return .systemFont(ofSize: size, weight: .semibold)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(styleHandler)
                .environment(\.font, Font.custom("Figtree", size: LemonadeTypography.shared.bodyMediumMedium.fontSize))
        }
    }
}

struct ContentView: View {
    @EnvironmentObject private var styleHandler: LemonadeStyleHandler

    var body: some View {
        NavigationStack {
            HomeView()
        }
        .lemonadeToastContainer()
        .lemonadeTooltipContainer()
        .onAppear { styleHandler.applyToWindows() }
    }
}
