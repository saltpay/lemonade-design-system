import SwiftUI
import Lemonade

@main
struct LemonadeSampleApp: App {
    init() {
        // Register Lemonade fonts at app startup
        LemonadeFonts.registerFonts()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        NavigationStack {
            HomeView()
        }
    }
}
