import SwiftUI
import UIKit

@MainActor
final class LemonadeStyleHandler: ObservableObject {
    @Published var currentStyle: LemonadeStyle = .system {
        didSet { applyToWindows() }
    }

    /// Applies the selected appearance to every window so that presented sheets
    /// (which don't inherit SwiftUI's `.preferredColorScheme`) stay in sync.
    func applyToWindows() {
        let style = currentStyle.userInterfaceStyle
        UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .forEach { $0.overrideUserInterfaceStyle = style }
    }
}
