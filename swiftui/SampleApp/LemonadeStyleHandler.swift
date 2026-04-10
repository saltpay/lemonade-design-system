import SwiftUI

final class LemonadeStyleHandler: ObservableObject {
    @Published var currentStyle: LemonadeStyle = .light
}
