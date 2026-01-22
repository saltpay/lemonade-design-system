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
        case .small: return LemonadeSizes.size400.value      // 16
        case .medium: return LemonadeSizes.size500.value     // 20
        case .large: return LemonadeSizes.size600.value      // 24
        case .xLarge: return LemonadeSizes.size800.value     // 32
        case .xxLarge: return LemonadeSizes.size1000.value   // 40
        case .xxxLarge: return LemonadeSizes.size1200.value  // 48
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

internal struct LemonadeCountryFlagView: View {
    let flag: LemonadeCountryFlag
    let size: LemonadeCountryFlagSize

    var body: some View {
        if let uiImage = UIImage(named: flag.rawValue, in: .lemonade, compatibleWith: nil) {
            Image(uiImage: uiImage)
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: size.value, height: size.value)
                .clipShape(Circle())
                .overlay(
                    Circle()
                        .stroke(
                            LemonadeTheme.colors.border.borderNeutralMedium,
                            lineWidth: LemonadeBorderWidthTokens().base.border25
                        )
                )
        } else {
            Circle()
                .fill(LemonadeTheme.colors.background.bgNeutralSubtle)
                .frame(width: size.value, height: size.value)
                .overlay(
                    Image(systemName: "flag.slash")
                        .foregroundColor(LemonadeTheme.colors.content.contentSecondary)
                        .font(.system(size: size.value * 0.4))
                )
        }
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
