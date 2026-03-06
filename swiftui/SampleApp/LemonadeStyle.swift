import Lemonade
import SwiftUI

enum LemonadeStyle: CaseIterable {
    case light, dark

    var label: String {
        switch self {
        case .light: "Light"
        case .dark: "Dark"
        }
    }

    var colorScheme: ColorScheme {
        switch self {
        case .light: .light
        case .dark: .dark
        }
    }

    var colors: LemonadeSemanticColors {
        switch self {
        case .light: LemonadeLightTheme()
        case .dark: LemonadeDarkTheme()
        }
    }
}
