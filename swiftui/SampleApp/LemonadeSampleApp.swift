import SwiftUI
import Lemonade

@main
struct LemonadeSampleApp: App {
    init() {
        // Register Lemonade fonts at app startup
        LemonadeFonts.registerFonts()
        
        // Customize the font for large titles
        UINavigationBar.appearance().largeTitleTextAttributes = [
            .font: UIFont(name: "Figtree-Semibold" ,size: LemonadeTypography.shared.headingLarge.fontSize)!
        ]
        
        // Customize the font for inline titles
        UINavigationBar.appearance().titleTextAttributes = [
            .font: UIFont(name: "Figtree-Semibold", size: LemonadeTypography.shared.headingXXSmall.fontSize)!
        ]
        
        // Customize the fon for search bar
        UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).font = UIFont(name: "Figtree", size: LemonadeTypography.shared.bodyMediumMedium.fontSize)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.font, Font.custom("Figtree", size: LemonadeTypography.shared.bodyMediumMedium.fontSize))
        }
    }
}

struct ContentView: View {
    var body: some View {
        NavigationStack {
            HomeView()
        }
        .lemonadeToastContainer()
    }
}
