import SwiftUI

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
    /// LemonadeUi.HorizontalDivider(isDashed: true)
    /// ```
    ///
    /// - Parameters:
    ///   - label: Optional String label to display in the center of the divider
    ///   - color: Color of the divider. Defaults to borderNeutralLow
    ///   - isDashed: Boolean flag to define if the divider is dashed or not
    /// - Returns: A styled horizontal divider view
    @ViewBuilder
    static func HorizontalDivider(
        label: String? = nil,
        color: Color = LemonadeTheme.colors.border.borderNeutralLow,
        isDashed: Bool = false
    ) -> some View {
        if let label = label {
            HStack(spacing: LemonadeTheme.spaces.spacing300) {
                CoreHorizontalDivider(color: color, isDashed: isDashed)

                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography().bodyXSmallRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )

                CoreHorizontalDivider(color: color, isDashed: isDashed)
            }
        } else {
            CoreHorizontalDivider(color: color, isDashed: isDashed)
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
    /// LemonadeUi.VerticalDivider(isDashed: true)
    /// ```
    ///
    /// - Parameters:
    ///   - color: Color of the divider. Defaults to borderNeutralLow
    ///   - isDashed: Boolean flag to define if the divider is dashed or not
    /// - Returns: A styled vertical divider view
    @ViewBuilder
    static func VerticalDivider(
        color: Color = LemonadeTheme.colors.border.borderNeutralLow,
        isDashed: Bool = false
    ) -> some View {
        CoreVerticalDivider(color: color, isDashed: isDashed)
    }
}

// MARK: - Core Divider Views

private struct CoreHorizontalDivider: View {
    let color: Color
    let isDashed: Bool

    var body: some View {
        GeometryReader { geometry in
            Path { path in
                path.move(to: CGPoint(x: 0, y: 0.5))
                path.addLine(to: CGPoint(x: geometry.size.width, y: 0.5))
            }
            .stroke(
                color,
                style: StrokeStyle(
                    lineWidth: 1,
                    dash: isDashed ? [4, 4] : []
                )
            )
        }
        .frame(height: 1)
    }
}

private struct CoreVerticalDivider: View {
    let color: Color
    let isDashed: Bool

    var body: some View {
        GeometryReader { geometry in
            Path { path in
                path.move(to: CGPoint(x: 0.5, y: 0))
                path.addLine(to: CGPoint(x: 0.5, y: geometry.size.height))
            }
            .stroke(
                color,
                style: StrokeStyle(
                    lineWidth: 1,
                    dash: isDashed ? [4, 4] : []
                )
            )
        }
        .frame(width: 1)
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
                LemonadeUi.HorizontalDivider(isDashed: true)
            }

            // Dashed horizontal divider with label
            VStack(alignment: .leading) {
                Text("Dashed Divider with Label")
                    .font(.caption)
                LemonadeUi.HorizontalDivider(label: "OR", isDashed: true)
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
                    LemonadeUi.VerticalDivider(isDashed: true)
                        .frame(height: 48)
                }
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
