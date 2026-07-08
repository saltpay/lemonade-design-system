import UIKit

enum LemonadeStyle: CaseIterable {
    case light, dark, system

    var label: String {
        switch self {
        case .light: "Light"
        case .dark: "Dark"
        case .system: "System"
        }
    }

    /// The UIKit interface style to force, or `.unspecified` to follow the system setting.
    var userInterfaceStyle: UIUserInterfaceStyle {
        switch self {
        case .light: .light
        case .dark: .dark
        case .system: .unspecified
        }
    }
}
