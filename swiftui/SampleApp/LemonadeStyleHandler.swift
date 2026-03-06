import SwiftUI
import Lemonade

final class LemonadeStyleHandler: ObservableObject {
    @Published var currentStyle: LemonadeStyle = .light {
        didSet { LemonadeTheme.colors = currentStyle.colors }
    }
}
