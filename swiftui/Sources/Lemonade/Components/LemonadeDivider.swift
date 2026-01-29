import SwiftUI

// MARK: - DividerVariant

/// Variants available for dividers.
public enum DividerVariant {
    /// Solid line divider.
    case solid

    /// Dashed line divider.
    case dashed
}

// MARK: - HorizontalDivider Component

public extension LemonadeUi {
    /// A horizontal divider to separate content. Optionally displays a label in the center.
    ///
    /// ## Usage
    /// ```swift
    /// // Simple divider
    /// LemonadeUi.HorizontalDivider()
    ///
    /// // Divider with label
    /// LemonadeUi.HorizontalDivider(label: "OR")
    ///
    /// // Dashed divider
    /// LemonadeUi.HorizontalDivider(variant: .dashed)
    /// ```
    ///
    /// - Parameters:
    ///   - label: Optional String label to display in the center of the divider
    ///   - variant: Variant of the divider. Defaults to `.solid`
    /// - Returns: A styled horizontal divider view
    @ViewBuilder
    static func HorizontalDivider(
        label: String? = nil,
        variant: DividerVariant = .solid
    ) -> some View {
        let thickness = LemonadeTheme.borderWidth.base.border25
        let dividerColor: Color = {
            switch variant {
            case .solid:
                return LemonadeTheme.colors.border.borderNeutralLow
            case .dashed:
                return LemonadeTheme.colors.border.borderNeutralMedium
            }
        }()

        if let label = label {
            HStack(spacing: 0) {
                CoreHorizontalDivider(
                    color: dividerColor,
                    variant: variant,
                    thickness: thickness
                )

                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography().bodySmallRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing300)

                CoreHorizontalDivider(
                    color: dividerColor,
                    variant: variant,
                    thickness: thickness
                )
            }
        } else {
            CoreHorizontalDivider(
                color: dividerColor,
                variant: variant,
                thickness: thickness
            )
        }
    }
}

// MARK: - VerticalDivider Component

public extension LemonadeUi {
    /// A vertical divider to separate content.
    ///
    /// ## Usage
    /// ```swift
    /// // Simple vertical divider
    /// LemonadeUi.VerticalDivider()
    ///
    /// // Dashed vertical divider
    /// LemonadeUi.VerticalDivider(variant: .dashed)
    /// ```
    ///
    /// - Parameters:
    ///   - variant: Variant of the divider. Defaults to `.solid`
    /// - Returns: A styled vertical divider view
    @ViewBuilder
    static func VerticalDivider(
        variant: DividerVariant = .solid
    ) -> some View {
        let thickness = LemonadeTheme.borderWidth.base.border25
        let dividerColor: Color = {
            switch variant {
            case .solid:
                return LemonadeTheme.colors.border.borderNeutralLow
            case .dashed:
                return LemonadeTheme.colors.border.borderNeutralMedium
            }
        }()

        CoreVerticalDivider(
            color: dividerColor,
            variant: variant,
            thickness: thickness
        )
    }
}

// MARK: - Core Divider Views

private struct CoreHorizontalDivider: View {
    let color: Color
    let variant: DividerVariant
    let thickness: CGFloat

    private var dashWidth: CGFloat { LemonadeTheme.sizes.size100 }
    private var dashGap: CGFloat { LemonadeTheme.spaces.spacing100 }

    var body: some View {
        GeometryReader { geometry in
            Path { path in
                path.move(to: CGPoint(x: 0, y: thickness / 2))
                path.addLine(to: CGPoint(x: geometry.size.width, y: thickness / 2))
            }
            .stroke(
                color,
                style: StrokeStyle(
                    lineWidth: thickness,
                    dash: variant == .dashed ? [dashWidth, dashGap] : []
                )
            )
        }
        .frame(height: thickness)
    }
}

private struct CoreVerticalDivider: View {
    let color: Color
    let variant: DividerVariant
    let thickness: CGFloat

    private var dashWidth: CGFloat { LemonadeTheme.sizes.size100 }
    private var dashGap: CGFloat { LemonadeTheme.spaces.spacing100 }

    var body: some View {
        GeometryReader { geometry in
            Path { path in
                path.move(to: CGPoint(x: thickness / 2, y: 0))
                path.addLine(to: CGPoint(x: thickness / 2, y: geometry.size.height))
            }
            .stroke(
                color,
                style: StrokeStyle(
                    lineWidth: thickness,
                    dash: variant == .dashed ? [dashWidth, dashGap] : []
                )
            )
        }
        .frame(width: thickness)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeDivider_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Simple horizontal divider
            VStack(alignment: .leading) {
                Text("Simple Divider")
                    .font(.caption)
                LemonadeUi.HorizontalDivider()
            }

            // Horizontal divider with label
            VStack(alignment: .leading) {
                Text("Divider with Label")
                    .font(.caption)
                LemonadeUi.HorizontalDivider(label: "OR")
            }

            // Dashed horizontal divider
            VStack(alignment: .leading) {
                Text("Dashed Divider")
                    .font(.caption)
                LemonadeUi.HorizontalDivider(variant: .dashed)
            }

            // Dashed horizontal divider with label
            VStack(alignment: .leading) {
                Text("Dashed Divider with Label")
                    .font(.caption)
                LemonadeUi.HorizontalDivider(label: "OR", variant: .dashed)
            }

            // Vertical dividers
            HStack(spacing: 24) {
                VStack {
                    Text("Simple")
                        .font(.caption)
                    LemonadeUi.VerticalDivider()
                        .frame(height: 48)
                }

                VStack {
                    Text("Dashed")
                        .font(.caption)
                    LemonadeUi.VerticalDivider(variant: .dashed)
                        .frame(height: 48)
                }
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
