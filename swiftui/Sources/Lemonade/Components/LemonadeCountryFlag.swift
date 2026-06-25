import SwiftUI

// MARK: - Country Flag Size

/// Size variants for the Country Flag component
public enum LemonadeCountryFlagSize {
    case small
    case medium
    case large
    case xLarge
    case xxLarge
    case xxxLarge
    case xxxxLarge

    /// Returns the CGFloat value for this size
    public var value: CGFloat {
        switch self {
        case .small: return LemonadeTheme.sizes.size400      // 16
        case .medium: return LemonadeTheme.sizes.size500     // 20
        case .large: return LemonadeTheme.sizes.size600      // 24
        case .xLarge: return LemonadeTheme.sizes.size800     // 32
        case .xxLarge: return LemonadeTheme.sizes.size1000   // 40
        case .xxxLarge: return LemonadeTheme.sizes.size1200  // 48
        case .xxxxLarge: return LemonadeTheme.sizes.size1400 // 56
        }
    }

    /// Corner radius applied when the flag uses `LemonadeCountryFlagShape.rounded`.
    var roundedRadius: CGFloat {
        switch self {
        case .small: return LemonadeTheme.radius.radius100      // 4
        case .medium: return LemonadeTheme.radius.radius150     // 6
        case .large: return LemonadeTheme.radius.radius200      // 8
        case .xLarge: return LemonadeTheme.radius.radius250     // 10
        case .xxLarge: return LemonadeTheme.radius.radius300    // 12
        case .xxxLarge: return LemonadeTheme.radius.radius400   // 16
        case .xxxxLarge: return LemonadeTheme.radius.radius500  // 20
        }
    }
}

// MARK: - Country Flag Shape

/// Shape variants for the Country Flag component.
public enum LemonadeCountryFlagShape {
    case circular
    case rounded
}

// MARK: - Country Flag Component

public extension LemonadeUi {
    /// Country Flags component, to display the available country flags in a standardized way.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.CountryFlag(
    ///     flag: .pTPortugal,
    ///     size: .medium,
    ///     shape: .rounded
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - flag: The `LemonadeCountryFlag` to be displayed
    ///   - size: The `LemonadeCountryFlagSize` to be applied. Defaults to `.medium`
    ///   - shape: The `LemonadeCountryFlagShape` to be applied. Defaults to `.circular`
    /// - Returns: A styled flag view
    @ViewBuilder
    static func CountryFlag(
        flag: LemonadeCountryFlag,
        size: LemonadeCountryFlagSize = .medium,
        shape: LemonadeCountryFlagShape = .circular
    ) -> some View {
        LemonadeCountryFlagView(flag: flag, size: size, shape: shape)
    }
}

// MARK: - Internal View

private struct LemonadeCountryFlagView: View {
    let flag: LemonadeCountryFlag
    let size: LemonadeCountryFlagSize
    let shape: LemonadeCountryFlagShape

    @ViewBuilder
    private var flagView: some View {
        let sized = flagImage
            .resizable()
            .aspectRatio(contentMode: .fill)
            .frame(width: size.value, height: size.value)

        switch shape {
        case .circular:
            styled(sized, with: Circle())
        case .rounded:
            styled(sized, with: RoundedRectangle(cornerRadius: size.roundedRadius))
        }
    }

    var body: some View {
        flagView
    }

    @ViewBuilder
    private func styled<S: Shape>(_ view: some View, with shape: S) -> some View {
        view
            .clipShape(shape)
            .overlay(
                shape.stroke(
                    LemonadeTheme.colors.border.borderNeutralMedium,
                    lineWidth: LemonadeTheme.borderWidth.base.border25
                )
            )
    }

    private var flagImage: Image {
        Image(flag.rawValue, bundle: .lemonade)
    }
}

// MARK: - Preview

#Preview("Country Flag Sizes") {
    VStack(spacing: 16) {
        HStack(spacing: 16) {
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .small)
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .medium)
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .large)
        }
        HStack(spacing: 16) {
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .xLarge)
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .xxLarge)
            LemonadeUi.CountryFlag(flag: .pTPortugal, size: .xxxLarge)
        }
    }
    .padding()
}

#Preview("Shapes") {
    HStack(spacing: 16) {
        LemonadeUi.CountryFlag(flag: .pTPortugal, size: .xxLarge, shape: .circular)
        LemonadeUi.CountryFlag(flag: .pTPortugal, size: .xxLarge, shape: .rounded)
    }
    .padding()
}

#Preview("Multiple Flags") {
    HStack(spacing: 12) {
        LemonadeUi.CountryFlag(flag: .bRBrazil, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .uSUnitedStates, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .gBUnitedKingdom, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .dEGermany, size: .xxLarge)
    }
    .padding()
}
