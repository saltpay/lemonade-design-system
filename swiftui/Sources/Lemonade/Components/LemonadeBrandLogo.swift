import SwiftUI

// MARK: - Brand Logo Size

/// Size variants for the Brand Logo component
public enum LemonadeBrandLogoSize {
    case small
    case medium
    case large
    case xLarge
    case xxLarge

    /// Returns the CGFloat value for this size
    public var value: CGFloat {
        switch self {
        case .small: return LemonadeSizes.size400.value      // 16
        case .medium: return LemonadeSizes.size500.value     // 20
        case .large: return LemonadeSizes.size600.value      // 24
        case .xLarge: return LemonadeSizes.size800.value     // 32
        case .xxLarge: return LemonadeSizes.size1000.value   // 40
        }
    }
}

// MARK: - Brand Logo Component

public extension LemonadeUi {
    /// Brand Logo component, to display Card Schemes in a standardized way.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.BrandLogo(
    ///     logo: .visa,
    ///     size: .medium
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - logo: The `LemonadeBrandLogo` to be displayed
    ///   - size: The `LemonadeBrandLogoSize` to be applied. Defaults to `.medium`
    /// - Returns: A styled brand logo view
    @ViewBuilder
    static func BrandLogo(
        logo: LemonadeBrandLogo,
        size: LemonadeBrandLogoSize = .medium
    ) -> some View {
        LemonadeBrandLogoView(logo: logo, size: size)
    }
}

// MARK: - Internal View

internal struct LemonadeBrandLogoView: View {
    let logo: LemonadeBrandLogo
    let size: LemonadeBrandLogoSize

    var body: some View {
        if let uiImage = UIImage(named: logo.rawValue, in: .lemonade, compatibleWith: nil) {
            Image(uiImage: uiImage)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: size.value, height: size.value)
        } else {
            Rectangle()
                .fill(LemonadeTheme.colors.background.bgNeutralSubtle)
                .frame(width: size.value, height: size.value)
                .overlay(
                    Image(systemName: "creditcard")
                        .foregroundStyle(LemonadeTheme.colors.content.contentSecondary)
                        .font(.system(size: size.value * 0.5))
                )
        }
    }
}

// MARK: - Preview

#Preview("Brand Logo Sizes") {
    VStack(spacing: 16) {
        HStack(spacing: 16) {
            LemonadeUi.BrandLogo(logo: .visa, size: .small)
            LemonadeUi.BrandLogo(logo: .visa, size: .medium)
            LemonadeUi.BrandLogo(logo: .visa, size: .large)
        }
        HStack(spacing: 16) {
            LemonadeUi.BrandLogo(logo: .visa, size: .xLarge)
            LemonadeUi.BrandLogo(logo: .visa, size: .xxLarge)
        }
    }
    .padding()
}

#Preview("Multiple Brand Logos") {
    HStack(spacing: 12) {
        LemonadeUi.BrandLogo(logo: .visa, size: .xxLarge)
        LemonadeUi.BrandLogo(logo: .mastercard, size: .xxLarge)
        LemonadeUi.BrandLogo(logo: .amex, size: .xxLarge)
        LemonadeUi.BrandLogo(logo: .applePay, size: .xxLarge)
    }
    .padding()
}
