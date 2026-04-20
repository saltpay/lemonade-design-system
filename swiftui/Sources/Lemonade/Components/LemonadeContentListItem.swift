import SwiftUI

// MARK: - LemonadeContentListItemLayout

/// Defines the layout arrangement for ContentListItem.
/// - `horizontal`: Label on the left, value on the right (same line)
/// - `vertical`: Label on top, value below
public enum LemonadeContentListItemLayout {
    case horizontal
    case vertical
}

// MARK: - Public API

public extension LemonadeUi {
    /// A display-only list item for showing label-value pairs.
    ///
    /// Supports horizontal (label left, value right) and vertical (label top, value bottom) layouts.
    /// In vertical layout, providing a `contentSlot` switches the value to a larger typography.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.ContentListItem(
    ///     label: "Account holder",
    ///     value: "John Doe"
    /// )
    ///
    /// LemonadeUi.ContentListItem(
    ///     label: "Balance",
    ///     value: "$1,234.56",
    ///     layout: .vertical
    /// ) {
    ///     LemonadeUi.Tag(label: "Available", voice: .positive)
    /// }
    /// ```
    ///
    /// - Parameters:
    ///   - label: Label String describing the data field
    ///   - value: Value String to display
    ///   - layout: Horizontal or vertical arrangement
    ///   - showDivider: Whether to display a bottom divider below the item
    ///   - leadingSlot: Optional leading element (e.g. SymbolContainer)
    ///   - trailingSlot: Optional trailing element (e.g. icon action)
    ///   - contentSlot: Optional additional content. In vertical layout, switches value to larger typography
    @ViewBuilder
    static func ContentListItem<Leading: View, Trailing: View, Content: View>(
        label: String,
        value: String,
        layout: LemonadeContentListItemLayout = .horizontal,
        showDivider: Bool = false,
        @ViewBuilder leadingSlot: @escaping () -> Leading = { EmptyView() },
        @ViewBuilder trailingSlot: @escaping () -> Trailing = { EmptyView() },
        @ViewBuilder contentSlot: @escaping () -> Content = { EmptyView() }
    ) -> some View {
        LemonadeContentListItemView(
            label: label,
            value: value,
            layout: layout,
            showDivider: showDivider,
            leadingSlot: leadingSlot,
            trailingSlot: trailingSlot,
            contentSlot: contentSlot
        )
    }
}

// MARK: - Internal View

private struct LemonadeContentListItemView<Leading: View, Trailing: View, Content: View>: View {
    let label: String
    let value: String
    let layout: LemonadeContentListItemLayout
    let showDivider: Bool
    @ViewBuilder let leadingSlot: () -> Leading
    @ViewBuilder let trailingSlot: () -> Trailing
    @ViewBuilder let contentSlot: () -> Content

    private var hasContentSlot: Bool {
        Content.self != EmptyView.self
    }

    private var hasTrailingSlot: Bool {
        Trailing.self != EmptyView.self
    }

    private var hasLeadingSlot: Bool {
        Leading.self != EmptyView.self
    }

    var body: some View {
        VStack(spacing: 0) {
            Group {
                switch layout {
                case .horizontal:
                    horizontalLayout
                case .vertical:
                    verticalLayout
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)

            if showDivider {
                LemonadeUi.HorizontalDivider()
                    .padding(.horizontal, LemonadeTheme.spaces.spacing400)
            }
        }
    }

    private var horizontalLayout: some View {
        HStack(alignment: .center, spacing: LemonadeTheme.spaces.spacing300) {
            if hasLeadingSlot {
                leadingSlot()
            }

            VStack(alignment: .leading, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodyMediumRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )

                if hasContentSlot {
                    contentSlot()
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            HStack(spacing: LemonadeTheme.spaces.spacing300) {
                LemonadeUi.Text(
                    value,
                    textStyle: LemonadeTypography.shared.bodyMediumMedium,
                    color: LemonadeTheme.colors.content.contentPrimary
                )
                .multilineTextAlignment(.trailing)

                if hasTrailingSlot {
                    trailingSlot()
                }
            }
        }
    }

    private var verticalLayout: some View {
        HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing300) {
            if hasLeadingSlot {
                leadingSlot()
            }

            VStack(alignment: .leading, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )

                HStack(spacing: LemonadeTheme.spaces.spacing100) {
                    LemonadeUi.Text(
                        value,
                        textStyle: hasContentSlot
                            ? LemonadeTypography.shared.bodyXLargeSemiBold
                            : LemonadeTypography.shared.bodyMediumMedium,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)

                    if hasTrailingSlot {
                        trailingSlot()
                    }
                }

                if hasContentSlot {
                    contentSlot()
                }
            }
        }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeContentListItem_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: LemonadeTheme.spaces.spacing400) {
                // Horizontal - simple
                LemonadeUi.ContentListItem(
                    label: "Account holder",
                    value: "John Doe"
                )

                // Horizontal - with leading and trailing
                LemonadeUi.ContentListItem(
                    label: "Label",
                    value: "Value",
                    leadingSlot: {
                        LemonadeUi.SymbolContainer(
                            icon: .heart,
                            contentDescription: nil,
                            size: .medium
                        )
                    },
                    trailingSlot: {
                        LemonadeUi.Icon(
                            icon: .pencilLine,
                            contentDescription: "Edit",
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentBrand
                        )
                    }
                )

                LemonadeUi.HorizontalDivider()

                // Vertical small
                LemonadeUi.ContentListItem(
                    label: "Balance",
                    value: "$1,234.56",
                    layout: .vertical
                )

                // Vertical large (with content slot)
                LemonadeUi.ContentListItem(
                    label: "Balance",
                    value: "$1,234.56",
                    layout: .vertical,
                    leadingSlot: {
                        LemonadeUi.SymbolContainer(
                            icon: .heart,
                            contentDescription: nil,
                            size: .medium
                        )
                    },
                    trailingSlot: {
                        LemonadeUi.Icon(
                            icon: .pencilLine,
                            contentDescription: "Edit",
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentBrand
                        )
                    },
                    contentSlot: {
                        LemonadeUi.Tag(label: "Available", voice: .positive)
                    }
                )

                // Stacked list with dividers
                VStack(spacing: 0) {
                    LemonadeUi.ContentListItem(
                        label: "Label",
                        value: "Value",
                        showDivider: true
                    )
                    LemonadeUi.ContentListItem(
                        label: "Label",
                        value: "Value",
                        showDivider: true
                    )
                    LemonadeUi.ContentListItem(
                        label: "Label",
                        value: "Value"
                    )
                }
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
