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

    /// Returns the CGFloat value for this size
    public var value: CGFloat {
        switch self {
        case .small: return LemonadeTheme.sizes.size400      // 16
        case .medium: return LemonadeTheme.sizes.size500     // 20
        case .large: return LemonadeTheme.sizes.size600      // 24
        case .xLarge: return LemonadeTheme.sizes.size800     // 32
        case .xxLarge: return LemonadeTheme.sizes.size1000   // 40
        case .xxxLarge: return LemonadeTheme.sizes.size1200  // 48
        }
    }
}

// MARK: - Country Flag Component

public extension LemonadeUi {
    /// Country Flags component, to display the available country flags in a standardized way.
    /// The flag is displayed in a circular shape with a border.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.CountryFlag(
    ///     flag: .pTPortugal,
    ///     size: .medium
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - flag: The `LemonadeCountryFlag` to be displayed
    ///   - size: The `LemonadeCountryFlagSize` to be applied. Defaults to `.medium`
    /// - Returns: A styled circular flag view
    @ViewBuilder
    static func CountryFlag(
        flag: LemonadeCountryFlag,
        size: LemonadeCountryFlagSize = .medium
    ) -> some View {
        LemonadeCountryFlagView(flag: flag, size: size)
    }
}

// MARK: - Internal View

private struct LemonadeCountryFlagView: View {
    let flag: LemonadeCountryFlag
    let size: LemonadeCountryFlagSize

    var body: some View {
        flagImage
            .resizable()
            .aspectRatio(contentMode: .fill)
            .frame(width: size.value, height: size.value)
            .clipShape(Circle())
            .overlay(
                Circle()
                    .stroke(
                        LemonadeTheme.colors.border.borderNeutralMedium,
                        lineWidth: LemonadeTheme.borderWidth.base.border25
                    )
            )
            .accessibilityLabel(flag.countryName)
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

#Preview("Multiple Flags") {
    HStack(spacing: 12) {
        LemonadeUi.CountryFlag(flag: .bRBrazil, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .uSUnitedStates, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .gBUnitedKingdom, size: .xxLarge)
        LemonadeUi.CountryFlag(flag: .dEGermany, size: .xxLarge)
    }
    .padding()
}
